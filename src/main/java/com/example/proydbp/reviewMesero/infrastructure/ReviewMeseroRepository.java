package com.example.proydbp.reviewMesero.infrastructure;

import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewMeseroRepository extends JpaRepository<ReviewMesero, Long> {
}
