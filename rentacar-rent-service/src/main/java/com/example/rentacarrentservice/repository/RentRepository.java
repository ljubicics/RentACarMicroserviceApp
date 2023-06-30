package com.example.rentacarrentservice.repository;

import com.example.rentacarrentservice.domain.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    Rent findRentById(Long id);
    List<Rent> findRentByCar_Id(Long id);

    Rent findRentByUserId(Long id);

    void deleteById(Long id);

    @Query(value = "select top 1 * from rent where Rent_userId = ? order by id desc ", nativeQuery = true)
    Rent findLastRentById(Long userId);
}
