package com.example.rentacarrentservice.controller;

import com.example.rentacarrentservice.domain.Business;
import com.example.rentacarrentservice.dto.*;
import com.example.rentacarrentservice.repository.ReviewRepository;
import com.example.rentacarrentservice.service.RentServiceSpecification;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/rentservice")
public class RentController {
    private RentServiceSpecification rentServiceSpecification;
    private ReviewRepository reviewRepository;

    public RentController(RentServiceSpecification rentServiceSpecification, ReviewRepository reviewRepository) {
        this.rentServiceSpecification = rentServiceSpecification;
        this.reviewRepository = reviewRepository;
    }

    @ApiOperation(value = "Add business")
    @PostMapping("/business")
    public ResponseEntity<BusinessDto> addBusiness(@RequestBody @Valid BusinessCreateDto businessCreateDto) {
        System.out.println(businessCreateDto.toString());
        return new ResponseEntity<>(rentServiceSpecification.addBusiness(businessCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add cars")
    @PostMapping("/cars")
    public ResponseEntity<CarDto> addCar(@RequestBody @Valid CarCreateDto carCreateDto) {
        return new ResponseEntity<>(rentServiceSpecification.addCar(carCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add rent")
    @PostMapping("/rent")
    public ResponseEntity<RentDto> addRent(@RequestBody @Valid RentCreateDto rentCreateDto) {
        return new ResponseEntity<>(rentServiceSpecification.addRent(rentCreateDto), HttpStatus.CREATED);
    }


    @ApiOperation(value = "Delete rent")
    @PostMapping("/deleterent/{id}")
    public ResponseEntity<Boolean> deleteRent(@PathVariable("id") Long id) {
        rentServiceSpecification.deleteRent(id);
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Find reviews")
    @GetMapping("/review/{id}/findAll")
    public ResponseEntity<Page<ReviewDto>> findAll(@PathVariable("id") Long id, @ApiIgnore Pageable pageable) {
        return new ResponseEntity<>(rentServiceSpecification.findAllByBusinessId(id, pageable), HttpStatus.OK);
    }
    @ApiOperation(value = "Add review")
    @PostMapping("/{id}/review")
    public ResponseEntity<ReviewDto> add(@PathVariable("id") Long id, @RequestBody @Valid ReviewCreateDto reviewCreateDto) {
        return new ResponseEntity<>(rentServiceSpecification.addCommentOnBusiness(id, reviewCreateDto), HttpStatus.OK);
    }
    @ApiOperation(value = "Get all cars")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping("/allcars")
    //@CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CLIENT"})
    public ResponseEntity<Page<CarDto>> getAllCars(@ApiIgnore Pageable pageable) {
        Page<CarDto> page = rentServiceSpecification.findAll(pageable);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }


}
