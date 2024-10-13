package com.example.proydbp.reviewDelivery.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryRequestDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewDelivery.infrastructure.ReviewDeliveryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReviewDeliveryService {

    final private ReviewDeliveryRepository reviewDeliveryRepository;
    final private ModelMapper modelMapper;
    final private ApplicationEventPublisher eventPublisher;

    @Autowired
    public ReviewDeliveryService(ReviewDeliveryRepository reviewDeliveryRepository,ModelMapper modelMapper,ApplicationEventPublisher eventPublisher) {
        this.reviewDeliveryRepository = reviewDeliveryRepository;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    public ReviewDeliveryResponseDto findReviewDeliveryById(Long id) {
        ReviewDelivery reviewDelivery = reviewDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada para el repartidor con id: " + id));
        return modelMapper.map(reviewDelivery,ReviewDeliveryResponseDto.class);
    }

    public List<ReviewDeliveryResponseDto> findAllReviewDelivery() {
        return reviewDeliveryRepository.findAll().stream()
                .map(review -> modelMapper.map(review, ReviewDeliveryResponseDto.class))
                .toList();
    }

    public ReviewDeliveryResponseDto createReviewDelivery(ReviewDeliveryRequestDto dto) {
        ReviewDelivery reviewDelivery = modelMapper.map(dto, ReviewDelivery.class);
        reviewDelivery.setFecha(LocalDate.now());
        reviewDelivery.setHora(LocalTime.now());

        ReviewDelivery savedReview = reviewDeliveryRepository.save(reviewDelivery);

        //String recipientEmail = savedReview.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new ReviewDeliveryCreatedEvent(savedReview,recipientEmail));

        return modelMapper.map(savedReview,ReviewDeliveryResponseDto.class);
    }

    public void deleteReviewDelivery(Long id) {
        ReviewDelivery reviewDelivery = reviewDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada para el repartidor con id: " + id));
        reviewDeliveryRepository.delete(reviewDelivery);
    }

    public ReviewDeliveryResponseDto updateReviewDelivery(Long id, ReviewDeliveryRequestDto dto) {
        ReviewDelivery existingReview = reviewDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada para el repartidor con id: \" + id"));

        if (dto.getComentario() != null){
            existingReview.setComentario(dto.getComentario());
        }
        if (dto.getCalificacion() != null){
            existingReview.setCalificacion(dto.getCalificacion());
        }

        ReviewDelivery updatedReview = reviewDeliveryRepository.save(existingReview);
        return modelMapper.map(updatedReview,ReviewDeliveryResponseDto.class);
    }
}
