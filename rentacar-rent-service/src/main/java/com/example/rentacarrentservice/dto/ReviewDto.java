package com.example.rentacarrentservice.dto;

public class ReviewDto {
    private Long id;
    private String text;
    private String username;
    private Integer businessRating;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
