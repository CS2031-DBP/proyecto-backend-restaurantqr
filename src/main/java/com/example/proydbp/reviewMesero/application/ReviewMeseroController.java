package com.example.proydbp.reviewMesero.application;

import com.example.proydbp.reviewMesero.domain.ReviewMeseroService;
import com.example.proydbp.reviewMesero.dto.PatchReviewMeseroDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroRequestDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviewmesero")
public class ReviewMeseroController {

    final private ReviewMeseroService reviewMeseroService;

    @Autowired
    public ReviewMeseroController(ReviewMeseroService reviewMeseroService) {
        this.reviewMeseroService = reviewMeseroService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewMeseroResponseDto> getReviewMeseroById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewMeseroService.findReviewMeseroById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReviewMeseroResponseDto>> getAllReviewMeseros() {
        return ResponseEntity.ok(reviewMeseroService.findAllReviewMeseros());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ReviewMeseroResponseDto> createReviewMesero(@Valid @RequestBody ReviewMeseroRequestDto dto) {
        ReviewMeseroResponseDto createdReview = reviewMeseroService.createReviewMesero(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewMesero(@PathVariable Long id) {
        reviewMeseroService.deleteReviewMesero(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ReviewMeseroResponseDto> updateReviewMesero(@PathVariable Long id, @Valid @RequestBody PatchReviewMeseroDto dto) {
        ReviewMeseroResponseDto updatedReview = reviewMeseroService.updateReviewMesero(id, dto);
        return ResponseEntity.ok(updatedReview);
    }
}