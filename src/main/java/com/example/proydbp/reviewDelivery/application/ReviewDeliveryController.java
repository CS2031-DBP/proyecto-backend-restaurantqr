package com.example.proydbp.reviewDelivery.application;

import com.example.proydbp.reviewDelivery.domain.ReviewDeliveryService;
import com.example.proydbp.reviewDelivery.dto.PatchReviewDeliveryDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryRequestDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviewdelivery")
public class ReviewDeliveryController {

    @Autowired
    private ReviewDeliveryService reviewDeliveryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDeliveryResponseDto> getReviewDeliveryById(@PathVariable Long id) {
        ReviewDeliveryResponseDto review = reviewDeliveryService.findReviewDeliveryById(id);
        return ResponseEntity.ok(review);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<ReviewDeliveryResponseDto>> getAllReviewDelivery() {
        List<ReviewDeliveryResponseDto> reviews = reviewDeliveryService.findAllReviewDelivery();
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ReviewDeliveryResponseDto> createReviewDelivery(@RequestBody ReviewDeliveryRequestDto dto) {
        ReviewDeliveryResponseDto createdReview = reviewDeliveryService.createReviewDelivery(dto);
        return ResponseEntity.ok(createdReview);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewDelivery(@PathVariable Long id) {
        reviewDeliveryService.deleteReviewDelivery(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDeliveryResponseDto> updateReviewDelivery(@PathVariable Long id, @RequestBody PatchReviewDeliveryDto dto) {
        ReviewDeliveryResponseDto updatedReview = reviewDeliveryService.updateReviewDelivery(id, dto);
        return ResponseEntity.ok(updatedReview);
    }

}
