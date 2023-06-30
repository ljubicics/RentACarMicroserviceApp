package com.example.rentacarrentservice.repository;

import com.example.rentacarrentservice.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Car findCarsByType(String carType);

    Car findCarByTypeAndName(String type, String name);
    @Query(value = "select * from Car where Car.business_name = ?", nativeQuery = true)
    List<Car> findCarsByBusiness(String business);

}
