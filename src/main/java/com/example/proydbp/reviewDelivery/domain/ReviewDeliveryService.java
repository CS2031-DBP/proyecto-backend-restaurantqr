package com.example.proydbp.reviewDelivery.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.events.email_event.ReviewDeliveryCreadoEvent;
import com.example.proydbp.events.email_event.ReviewDeliveryDeleteEvent;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.domain.RepartidorService;
import com.example.proydbp.repartidor.infrastructure.RepartidorRepository;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryRequestDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewDelivery.infrastructure.ReviewDeliveryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ReviewDeliveryService {

    final private ReviewDeliveryRepository reviewDeliveryRepository;
    final private ModelMapper modelMapper;
    final private ApplicationEventPublisher eventPublisher;
    private final ClientRepository clientRepository;
    private final DeliveryRepository deliveryRepository;
    private final AuthorizationUtils authorizationUtils;
    private final RepartidorRepository repartidorRepository;
    private final RepartidorService repartidorService;

    @Autowired
    public ReviewDeliveryService (ReviewDeliveryRepository reviewDeliveryRepository, AuthorizationUtils authorizationUtils,
                                  ModelMapper modelMapper, ApplicationEventPublisher eventPublisher,
                                  ClientRepository clientRepository, DeliveryRepository deliveryRepository,
                                  RepartidorRepository repartidorRepository, RepartidorService repartidorService) {
        this.reviewDeliveryRepository = reviewDeliveryRepository;
        this.modelMapper = modelMapper;
        this.authorizationUtils = authorizationUtils;
        this.eventPublisher = eventPublisher;
        this.clientRepository = clientRepository;
        this.deliveryRepository = deliveryRepository;
        this.repartidorRepository = repartidorRepository;
        this.repartidorService = repartidorService;
    }

    public ReviewDeliveryResponseDto findReviewDeliveryById(Long id) {
        ReviewDelivery reviewDelivery = reviewDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review de repartidor con id " + id + " no encontrada"));
        return modelMapper.map(reviewDelivery, ReviewDeliveryResponseDto.class);
    }

    public List<ReviewDeliveryResponseDto> findAllReviewDelivery() {
        return reviewDeliveryRepository.findAll().stream()
                .map(review -> modelMapper.map(review, ReviewDeliveryResponseDto.class))
                .toList();
    }

    public ReviewDeliveryResponseDto createReviewDelivery(ReviewDeliveryRequestDto dto) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con nombre de usuario " + username + " no encontrado"));

        ReviewDelivery reviewDelivery = new ReviewDelivery();
        reviewDelivery.setFecha(ZonedDateTime.now());
        reviewDelivery.setRatingScore(dto.getRatingScore());
        reviewDelivery.setClient(client);
        reviewDelivery.setRatingScore(dto.getRatingScore());
        reviewDelivery.setComment(dto.getComment());

        Repartidor repartidor = repartidorRepository.findById(dto.getRepartidorId())
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con id " + dto.getRepartidorId() + " no encontrado"));

        reviewDelivery.setRepartidor(repartidor);

        ReviewDelivery savedReview = reviewDeliveryRepository.save(reviewDelivery);

        repartidorService.updateRatingScore(repartidor.getId());

        // Publicar evento de creación de la reseña
        eventPublisher.publishEvent(new ReviewDeliveryCreadoEvent(savedReview, repartidor.getEmail()));

        return modelMapper.map(savedReview, ReviewDeliveryResponseDto.class);
    }

    public void deleteReviewDelivery(Long id) {
        ReviewDelivery existingReview = reviewDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review de repartidor con id " + id + " no encontrada"));

        String recipientEmail = existingReview.getRepartidor().getEmail();

        reviewDeliveryRepository.deleteById(id);

        // Publicar evento de eliminación de la reseña
        eventPublisher.publishEvent(new ReviewDeliveryDeleteEvent(existingReview, recipientEmail));

    }
}
