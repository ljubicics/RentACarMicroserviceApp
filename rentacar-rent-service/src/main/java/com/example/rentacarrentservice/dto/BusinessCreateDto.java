package com.example.rentacarrentservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BusinessCreateDto {
    @NotBlank
    private String city;
    @NotBlank
    private String description;
    @NotBlank
    private String businessName;
    @NotNull
    private Integer numberOfVehicles;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public void setNumberOfVehicles(Integer numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }
}
