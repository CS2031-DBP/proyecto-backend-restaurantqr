package com.example.proydbp.reviewMesero.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesero.domain.Mesero;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchReviewMeseroDto {

    @Column(nullable = false)
    private Double ratingScore;

    @Size(min = 0, max = 250)
    private String comment;

}
