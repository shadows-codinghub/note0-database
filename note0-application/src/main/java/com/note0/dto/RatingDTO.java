package com.note0.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RatingDTO {
    
    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer score;
    
    // Constructors
    public RatingDTO() {}
    
    public RatingDTO(Integer score) {
        this.score = score;
    }
    
    // Getters and Setters
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
}
