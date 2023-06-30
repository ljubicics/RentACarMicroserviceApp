package com.example.rentacarrentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class ReviewCreateDto {
    @NotEmpty(message = "Text cant be empty")
    private String text;
    @NotEmpty(message = "Username cant be empty")
    private String username;
    @Min(value = 1)
    @Max(value = 5)
    @JsonProperty("business_rating")
    private Integer businessRating;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getBusinessRating() {
        return businessRating;
    }

    public void setBusinessRating(Integer businessRating) {
        this.businessRating = businessRating;
    }
}
