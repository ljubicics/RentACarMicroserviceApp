package com.example.rentacarrentservice.repository;

import com.example.rentacarrentservice.domain.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Business findBusinessByBusinessName(String businessName);
    Business findBusinessById(Long id);
}
