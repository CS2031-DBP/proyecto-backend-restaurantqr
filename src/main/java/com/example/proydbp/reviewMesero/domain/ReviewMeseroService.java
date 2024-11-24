package com.example.proydbp.reviewMesero.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.events.email_event.ReviewMeseroCreadoEvent;
import com.example.proydbp.events.email_event.ReviewMeseroDeleteEvent;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.domain.MeseroService;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroRequestDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import com.example.proydbp.reviewMesero.infrastructure.ReviewMeseroRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ReviewMeseroService {

    final private ReviewMeseroRepository reviewMeseroRepository;
    final private ModelMapper modelMapper;
    final private ApplicationEventPublisher eventPublisher;
    private final ClientRepository clientRepository;
    private final PedidoLocalRepository pedidoLocalRepository;
    private final MeseroRepository meseroRepository;
    private final MeseroService meseroService;
    private final AuthorizationUtils authorizationUtils;

    @Autowired
    public ReviewMeseroService (ReviewMeseroRepository reviewMeseroRepository, ModelMapper modelMapper,
                                AuthorizationUtils authorizationUtils, ApplicationEventPublisher eventPublisher,
                                ClientRepository clientRepository, PedidoLocalRepository pedidoLocalRepository,
                                MeseroRepository meseroRepository, MeseroService meseroService) {
        this.reviewMeseroRepository = reviewMeseroRepository;
        this.authorizationUtils = authorizationUtils;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
        this.clientRepository = clientRepository;
        this.pedidoLocalRepository = pedidoLocalRepository;
        this.meseroRepository = meseroRepository;
        this.meseroService = meseroService;
    }

    public ReviewMeseroResponseDto findReviewMeseroById(Long id) {
        ReviewMesero reviewMesero = reviewMeseroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña de mesero con id " + id + " no encontrada"));
        return modelMapper.map(reviewMesero, ReviewMeseroResponseDto.class);
    }

    public Page<ReviewMeseroResponseDto> findAllReviewMeseros(int page, int size) {
        // Crear el objeto Pageable con los parámetros de página y tamaño
        Pageable pageable = PageRequest.of(page, size);

        // Obtener las reseñas de los meseros de forma paginada
        Page<ReviewMesero> reviewsPage = reviewMeseroRepository.findAll(pageable);

        // Convertir las reseñas a ReviewMeseroResponseDto y devolver el resultado paginado
        return reviewsPage.map(review -> modelMapper.map(review, ReviewMeseroResponseDto.class));
    }


    public ReviewMeseroResponseDto createReviewMesero(ReviewMeseroRequestDto dto) {

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        ReviewMesero reviewMesero = new ReviewMesero();
        reviewMesero.setFecha(ZonedDateTime.now());
        reviewMesero.setRatingScore(dto.getRatingScore());

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con nombre de usuario " + username + " no encontrado"));

        reviewMesero.setClient(client);
        reviewMesero.setRatingScore(dto.getRatingScore());
        reviewMesero.setComment(dto.getComment());

        Mesero mesero = meseroRepository.findById(dto.getMeseroId())
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con id " + dto.getMeseroId() + " no encontrado"));

        reviewMesero.setMesero(mesero);

        ReviewMesero savedReview = reviewMeseroRepository.save(reviewMesero);

        meseroService.updateRatingScore(mesero.getId());

        // Publicar evento
        String recipientEmail = mesero.getEmail();
        eventPublisher.publishEvent(new ReviewMeseroCreadoEvent(savedReview, recipientEmail));

        return modelMapper.map(savedReview, ReviewMeseroResponseDto.class);
    }

    public void deleteReviewMesero(Long id) {

        if (!reviewMeseroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reseña de mesero con id " + id + " no encontrada");
        }
        ReviewMesero reviewMesero = reviewMeseroRepository.findById(id).get();

        String recipientEmail = reviewMesero.getMesero().getEmail();

        reviewMeseroRepository.deleteById(id);

        eventPublisher.publishEvent(new ReviewMeseroDeleteEvent(reviewMesero, recipientEmail));
    }
}
