package com.note0.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.note0.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    
    Optional<Rating> findByMaterialIdAndUserId(String materialId, String userId);
    
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.material.id = :materialId")
    Double findAverageRatingByMaterialId(@Param("materialId") String materialId);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.material.id = :materialId")
    Long countRatingsByMaterialId(@Param("materialId") String materialId);
    
}
