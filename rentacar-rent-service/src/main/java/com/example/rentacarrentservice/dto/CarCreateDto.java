package com.example.rentacarrentservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CarCreateDto {
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @NotNull
    private String price;
    @NotBlank
    private String manufacturer;
    @NotBlank
    private String businessName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
