package com.example.proydbp.reviewDelivery.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
//import com.example.proydbp.events.email_event.ReviewDeliveryCreatedEvent;
import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.reviewDelivery.dto.PatchReviewDeliveryDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryRequestDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewDelivery.infrastructure.ReviewDeliveryRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReviewDeliveryService {

    final private ReviewDeliveryRepository reviewDeliveryRepository;
    final private ModelMapper modelMapper;
    final private ApplicationEventPublisher eventPublisher;
    private final ClientRepository clientRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public ReviewDeliveryService (ReviewDeliveryRepository reviewDeliveryRepository,
                                ModelMapper modelMapper, ApplicationEventPublisher eventPublisher, ClientRepository clientRepository, DeliveryRepository deliveryRepository) {
        this.reviewDeliveryRepository = reviewDeliveryRepository;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
        this.clientRepository = clientRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public ReviewDeliveryResponseDto findReviewDeliveryById(Long id) {
        ReviewDelivery reviewDelivery = reviewDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReviewRepartidor not found"));
        return modelMapper.map(reviewDelivery, ReviewDeliveryResponseDto.class);
    }

    public List<ReviewDeliveryResponseDto> findAllReviewDelivery() {
        return reviewDeliveryRepository.findAll().stream()
                .map(review -> modelMapper.map(review, ReviewDeliveryResponseDto.class))
                .toList();
    }

    public ReviewDeliveryResponseDto createReviewDelivery(ReviewDeliveryRequestDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with email " + username));

        ReviewDelivery reviewDelivery = new ReviewDelivery();
        reviewDelivery.setFecha(LocalDate.now());
        reviewDelivery.setHora(LocalTime.now());
        reviewDelivery.setClient(client);
        reviewDelivery.setCalificacion(dto.getRatingScore());
        reviewDelivery.setComentario(dto.getComment());

        Delivery delivery = deliveryRepository.findById(dto.getIdDelivery())
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        reviewDelivery.setDelivery(delivery);
        reviewDelivery.setRepartidor(delivery.getRepartidor());

        ReviewDelivery savedReview = reviewDeliveryRepository.save(reviewDelivery);

        String recipientEmail = savedReview.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new ReviewDeliveryCreatedEvent(savedReview, recipientEmail));

        return modelMapper.map(savedReview, ReviewDeliveryResponseDto.class);
    }



    public void deleteReviewRepartidor(Long id) {
        if (!reviewDeliveryRepository.existsById(id)) {
            throw new ResourceNotFoundException("ReviewRepartidor not found");
        }
        reviewDeliveryRepository.deleteById(id);
    }

    public ReviewDeliveryResponseDto updateReviewRepartidor(Long id, @Valid PatchReviewDeliveryDto dto) {
        ReviewDelivery existingReview = reviewDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReviewRepartidor not found"));

        if (dto.getCalificacion() != null) {
            existingReview.setCalificacion(dto.getCalificacion());
        }

        existingReview.setComentario(dto.getComentario());

        ReviewDelivery updatedReview = reviewDeliveryRepository.save(existingReview);
        return modelMapper.map(updatedReview, ReviewDeliveryResponseDto.class);
    }
}
