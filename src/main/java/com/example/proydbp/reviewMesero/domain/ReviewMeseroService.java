package com.example.proydbp.reviewMesero.domain;

import com.example.proydbp.events.email_event.ReviewMeseroCreatedEvent;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.reviewMesero.dto.PatchReviewMeseroDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroRequestDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import com.example.proydbp.reviewMesero.infrastructure.ReviewMeseroRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReviewMeseroService {

    final private ReviewMeseroRepository reviewMeseroRepository;
    final private ModelMapper modelMapper;
    final private ApplicationEventPublisher eventPublisher;

    @Autowired
    public ReviewMeseroService (ReviewMeseroRepository reviewMeseroRepository,
                                ModelMapper modelMapper, ApplicationEventPublisher eventPublisher) {
        this.reviewMeseroRepository = reviewMeseroRepository;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    public ReviewMeseroResponseDto findReviewMeseroById(Long id) {
        ReviewMesero reviewMesero = reviewMeseroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReviewMesero not found"));
        return modelMapper.map(reviewMesero, ReviewMeseroResponseDto.class);
    }

    public List<ReviewMeseroResponseDto> findAllReviewMeseros() {
        return reviewMeseroRepository.findAll().stream()
                .map(review -> modelMapper.map(review, ReviewMeseroResponseDto.class))
                .toList();
    }

    public ReviewMeseroResponseDto createReviewMesero(ReviewMeseroRequestDto dto) {
        ReviewMesero reviewMesero = modelMapper.map(dto, ReviewMesero.class);
        reviewMesero.setFecha(LocalDate.now());
        reviewMesero.setHora(LocalTime.now());

        ReviewMesero savedReview = reviewMeseroRepository.save(reviewMesero);

        String recipientEmail = savedReview.getMesero().getEmail();

        eventPublisher.publishEvent(new ReviewMeseroCreatedEvent(savedReview, recipientEmail));

        return modelMapper.map(savedReview, ReviewMeseroResponseDto.class);
    }


    public void deleteReviewMesero(Long id) {
        if (!reviewMeseroRepository.existsById(id)) {
            throw new ResourceNotFoundException("ReviewMesero not found");
        }
        reviewMeseroRepository.deleteById(id);
    }

    public ReviewMeseroResponseDto updateReviewMesero(Long id, @Valid PatchReviewMeseroDto dto) {
        ReviewMesero existingReview = reviewMeseroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReviewMesero not found"));

        if (dto.getRatingScore() != null) {
            existingReview.setRatingScore(dto.getRatingScore());
        }

        ReviewMesero updatedReview = reviewMeseroRepository.save(existingReview);
        return modelMapper.map(updatedReview, ReviewMeseroResponseDto.class);
    }
}
