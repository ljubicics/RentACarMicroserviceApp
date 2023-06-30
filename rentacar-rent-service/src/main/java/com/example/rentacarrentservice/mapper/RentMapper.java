package com.example.rentacarrentservice.mapper;

import com.example.rentacarrentservice.domain.*;
import com.example.rentacarrentservice.dto.*;
import com.example.rentacarrentservice.repository.BusinessRepository;
import com.example.rentacarrentservice.repository.CarRepository;
import org.springframework.stereotype.Component;

@Component
public class RentMapper {
    private BusinessRepository businessRepository;
    private CarRepository carRepository;

    public RentMapper(BusinessRepository businessRepository, CarRepository carRepository) {
        this.businessRepository = businessRepository;
        this.carRepository = carRepository;
    }

    public BusinessDto businessToBusinessDto(Business business) {
        BusinessDto businessDto = new BusinessDto();
        businessDto.setBusinessName(business.getBusinessName());
        businessDto.setCity(business.getCity());
        businessDto.setDescription(business.getDescription());
        businessDto.setNumberOfVehicles(business.getNumberOfVehicles());

        return businessDto;
    }

    public Business businessCreateDtoToBusiness(BusinessCreateDto businessCreateDto) {
        Business business = new Business();
        business.setBusinessName(businessCreateDto.getBusinessName());
        business.setCity(businessCreateDto.getCity());
        business.setDescription(businessCreateDto.getDescription());
        business.setNumberOfVehicles(businessCreateDto.getNumberOfVehicles());

        return business;
    }

    public CarDto carToCarDto(Car car){
        CarDto carDto = new CarDto();
        carDto.setName(car.getName());
        carDto.setType(car.getType());
        carDto.setPrice(car.getPrice());
        carDto.setManufacturer(car.getManufacturer());
        carDto.setBusinessName(car.getBusiness().getBusinessName());

        return carDto;
    }

    public Car carCreateDtoToCar(CarCreateDto carCreateDto){
        Car car = new Car();
        car.setPrice(carCreateDto.getPrice());
        car.setName(carCreateDto.getName());
        car.setType(carCreateDto.getType());
        car.setManufacturer(carCreateDto.getManufacturer());
        car.setBusiness(businessRepository.findBusinessByBusinessName(carCreateDto.getBusinessName()));

        return car;
    }

    public Rent rentCreateDtoToRent(RentCreateDto rentCreateDto) {
        Rent rent = new Rent();
        rent.setUserId(rentCreateDto.getUserid());
        rent.setRentStart(rentCreateDto.getRentStart());
        rent.setRentEnd(rentCreateDto.getRentEnd());
        rent.setBusinessName(rentCreateDto.getBusinessName());
        rent.setCity(rentCreateDto.getCity());
        rent.setCar(carRepository.findCarByTypeAndName(rentCreateDto.getCarType(), rentCreateDto.getCarName()));

        return rent;
    }

    public RentDto rentToRentDto(Rent rent) {
        RentDto rentDto = new RentDto();
        rentDto.setRentStart(rent.getRentStart());
        rentDto.setRentEnd(rent.getRentEnd());
        rentDto.setBusinessName(rent.getBusinessName());
        rentDto.setCity(rent.getCity());
        rentDto.setCarType(rent.getCar().getType());
        rentDto.setCarName(rent.getCar().getName());
        rentDto.setPrice(rent.getPrice());

        return rentDto;
    }

    public ReviewDto reviewToReviewDto(Review review){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setUsername(review.getUsername());
        reviewDto.setBusinessRating(review.getBusinessRating());
        reviewDto.setText(review.getText());

        return reviewDto;
    }

    public Review reviewCreateDtoToReview(ReviewCreateDto reviewCreateDto, Business business){
        Review review = new Review();
        review.setText(reviewCreateDto.getText());
        review.setUsername(reviewCreateDto.getUsername());
        review.setBusinessRating(reviewCreateDto.getBusinessRating());
        review.setBusiness(business);

        return review;
    }

}
