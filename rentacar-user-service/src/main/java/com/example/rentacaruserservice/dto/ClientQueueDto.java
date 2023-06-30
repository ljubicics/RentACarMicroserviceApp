package com.example.rentacaruserservice.dto;

public class ClientQueueDto {
    private Long userId;
    private Long rentId;
    private boolean increment;
    private Integer daysToIncrement;
    private String businessName;

    public Integer getDaysToIncrement() {
        return daysToIncrement;
    }

    public void setDaysToIncrement(Integer daysToIncrement) {
        this.daysToIncrement = daysToIncrement;
    }

    public boolean isIncrement() {
        return increment;
    }

    public void setIncrement(boolean increment) {
        this.increment = increment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Long getRentId() {
        return rentId;
    }

    public void setRentId(Long rentId) {
        this.rentId = rentId;
    }
}
