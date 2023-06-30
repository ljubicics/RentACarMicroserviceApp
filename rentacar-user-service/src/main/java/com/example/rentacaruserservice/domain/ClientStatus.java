package com.example.rentacaruserservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClientStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rank;
    private Integer minNumberOfDaysRented;
    private Integer maxNumberOfDaysRented;
    private Integer discount;

    public ClientStatus() {}

    public ClientStatus(String rank, Integer minNumberOfDaysRented, Integer maxNumberOfDaysRented, Integer discount) {
        this.rank = rank;
        this.minNumberOfDaysRented = minNumberOfDaysRented;
        this.maxNumberOfDaysRented = maxNumberOfDaysRented;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinNumberOfDaysRented() {
        return minNumberOfDaysRented;
    }

    public void setMinNumberOfDaysRented(Integer minNumberOfDaysRented) {
        this.minNumberOfDaysRented = minNumberOfDaysRented;
    }

    public Integer getMaxNumberOfDaysRented() {
        return maxNumberOfDaysRented;
    }

    public void setMaxNumberOfDaysRented(Integer maxNumberOfDaysRented) {
        this.maxNumberOfDaysRented = maxNumberOfDaysRented;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

}
