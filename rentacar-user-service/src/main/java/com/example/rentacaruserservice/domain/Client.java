package com.example.rentacaruserservice.domain;

import javax.persistence.*;
@Entity
public class Client extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String passportNumber;
    private Integer numberOfDaysRented;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Integer getNumberOfDaysRented() {
        return numberOfDaysRented;
    }

    public void setNumberOfDaysRented(Integer numberOfDaysRented) {
        this.numberOfDaysRented = numberOfDaysRented;
    }
}
