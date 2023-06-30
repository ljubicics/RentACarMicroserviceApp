package com.example.rentacarrentservice.service;

import com.example.rentacarrentservice.dto.*;
import com.example.rentacarrentservice.userservice.dto.ClientQueueDto;
import com.example.rentacarrentservice.userservice.dto.EmailInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RentServiceSpecification {
    BusinessDto addBusiness(BusinessCreateDto businessCreateDto);
    CarDto addCar(CarCreateDto carCreateDto);
    RentDto addRent(RentCreateDto rentCreateDto);
    Page<CarDto> findAll(Pageable pageable);
    void deleteRent(Long id);
    List<CarDto> availableCars(FindCarDto findCarDto);
    Page<ReviewDto> findAllByBusinessId(Long id, Pageable pageable);
    ReviewDto addCommentOnBusiness(Long id, ReviewCreateDto reviewCreateDto);
    void forwardClientAndRent(EmailInfoDto emailInfoDto);
}
