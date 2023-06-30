package com.example.rentacarrentservice.service.implementation;

import com.example.rentacarrentservice.domain.*;
import com.example.rentacarrentservice.dto.*;
import com.example.rentacarrentservice.exception.NotFoundException;
import com.example.rentacarrentservice.listener.helper.MessageHelper;
import com.example.rentacarrentservice.mapper.RentMapper;
import com.example.rentacarrentservice.repository.*;
import com.example.rentacarrentservice.service.RentServiceSpecification;
import com.example.rentacarrentservice.userservice.dto.ClientQueueDto;
import com.example.rentacarrentservice.userservice.dto.ClientStatusDto;
import com.example.rentacarrentservice.userservice.dto.EmailInfoDto;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional
public class RentServiceImplementation implements RentServiceSpecification {
    private BusinessRepository businessRepository;
    private RentMapper rentMapper;
    private final CarRepository carRepository;
    private Retry userServiceRetry;
    private RestTemplate userServiceRestTemplate;
    private RentRepository rentRepository;
    private JmsTemplate jmsTemplate;
    private String rentNumberDestination;
    private MessageHelper messageHelper;
    private final ReviewRepository reviewRepository;
    private String forwardClientRentDestination;

    public RentServiceImplementation(BusinessRepository businessRepository, RentMapper rentMapper,
                                     CarRepository carRepository, Retry userServiceRetry, RestTemplate userServiceClientConfiguration,
                                     RentRepository rentRepository,JmsTemplate jmsTemplate ,@Value("${destination.rentNumber}") String rentNumberDestination,
                                     MessageHelper messageHelper, ReviewRepository reviewRepository,
                                     @Value("${destination.forwardClientRent}") String forwardClientRentDestination) {
        this.businessRepository = businessRepository;
        this.rentMapper = rentMapper;
        this.carRepository = carRepository;
        this.userServiceRetry = userServiceRetry;
        this.userServiceRestTemplate = userServiceClientConfiguration;
        this.rentRepository = rentRepository;
        this.jmsTemplate = jmsTemplate;
        this.rentNumberDestination = rentNumberDestination;
        this.messageHelper = messageHelper;
        this.reviewRepository = reviewRepository;
        this.forwardClientRentDestination =forwardClientRentDestination;
    }

    @Override
    public BusinessDto addBusiness(BusinessCreateDto businessCreateDto) {
        Business business = rentMapper.businessCreateDtoToBusiness(businessCreateDto);
        businessRepository.save(business);
        return rentMapper.businessToBusinessDto(business);
    }

    @Override
    public CarDto addCar(CarCreateDto carCreateDto) {
        Car car = rentMapper.carCreateDtoToCar(carCreateDto);
        Business business = businessRepository.findBusinessByBusinessName(carCreateDto.getBusinessName());
        business.setNumberOfVehicles(business.getNumberOfVehicles() + 1);
        carRepository.save(car);
        return rentMapper.carToCarDto(car);
    }

    @Override
    public RentDto addRent(RentCreateDto rentCreateDto) {
        Rent rent = rentMapper.rentCreateDtoToRent(rentCreateDto);

        ClientStatusDto discountDto = Retry.decorateSupplier(userServiceRetry, () -> getDiscount(rentCreateDto.getUserid())).get();

        LocalDate start = rent.getRentStart();
        LocalDate end = rent.getRentEnd();
        long difference = DAYS.between(start, end);

        List<Car> cars = carRepository.findAll();
        String carPrice = "";
        Car wantedCar = null;
        for(Car car: cars) {
            if(car.getName().equalsIgnoreCase(rentCreateDto.getCarName())) {
                wantedCar = car;
                carPrice = car.getPrice();
                break;
            }
        }

        Double newPrice = difference * Double.parseDouble(carPrice) * (100 - discountDto.getDiscount()) / 100;
        rent.setPrice(String.valueOf(newPrice));
        rent.setCar(wantedCar);

        rentRepository.save(rent);

        Rent rentForId = rentRepository.findRentByUserId(rentCreateDto.getUserid());

        ClientQueueDto clientQueueDto = new ClientQueueDto();
        clientQueueDto.setUserId(rentForId.getUserId());
        clientQueueDto.setBusinessName(rentCreateDto.getBusinessName());
        clientQueueDto.setRentId(rentForId.getId());
        clientQueueDto.setDaysToIncrement((int) difference);
        clientQueueDto.setIncrement(true);

        jmsTemplate.convertAndSend(rentNumberDestination, messageHelper.createTextMessage(clientQueueDto));
        return rentMapper.rentToRentDto(rent);
    }

    private ClientStatusDto getDiscount(Long id) {
        System.out.println("Getting user with id: " + id);
        try {
            return userServiceRestTemplate.exchange("/users/" +
                    id + "/discount", HttpMethod.GET, null, ClientStatusDto.class).getBody();
        } catch (HttpClientErrorException e) {
            throw new NotFoundException(String.format("User with that id is: %d not found.", id));
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }

    @Override
    public Page<CarDto> findAll(Pageable pageable) {
        return carRepository.findAll(pageable)
                .map(rentMapper::carToCarDto);
    }

    @Override
    public List<CarDto> availableCars(FindCarDto findCarDto) {

            List<Car> poTerminu= new ArrayList<>();
            List<Car> poFirmi= new ArrayList<>();
            List<Car> out= new ArrayList<>();

            if (!findCarDto.getBusinessName().equals("")){
                for (Car c:carRepository.findAll()) {
                    if (c.getBusiness().getBusinessName().equals(findCarDto.getBusinessName())) poFirmi.add(c);
                }
            }
            else {
                poFirmi= carRepository.findAll();
            }
            if (!findCarDto.getStartDate().equals("") && !findCarDto.getEndDate().equals("")){

                for (Car c:carRepository.findAll()) {
                    boolean dodaj= true;
                    // ako sve rez za vozilo ispunjavaju uslov dodaj vozilo
                    for (Rent r : rentRepository.findRentByCar_Id(c.getId())) {
                        if (!((r.getRentStart().isBefore(findCarDto.getStartDate())
                                && r.getRentEnd().isBefore(findCarDto.getStartDate())) ||
                                (r.getRentStart().isAfter(findCarDto.getEndDate()) &&
                                        r.getRentEnd().isAfter(findCarDto.getEndDate())))){
                            dodaj=false;
                        }
                    }
                    poTerminu.add(c);
                }
            }
            else {
                poTerminu= carRepository.findAll();
            }

            for (Car v:carRepository.findAll()) {
                if (poTerminu.contains(v) && poFirmi.contains(v)){
                    out.add(v);
                }
            }
            return out.stream().map(rentMapper::carToCarDto).toList();
    }

    @Override
    public Page<ReviewDto> findAllByBusinessId(Long id, Pageable pageable) {
        return reviewRepository.findAllByBusiness_Id(id, pageable).map(rentMapper::reviewToReviewDto);

    }

    @Override
    public ReviewDto addCommentOnBusiness(Long id, ReviewCreateDto reviewCreateDto) {
        Business business = businessRepository.findBusinessById(id);
        Review review = reviewRepository.save(rentMapper.reviewCreateDtoToReview(reviewCreateDto, business));
        return rentMapper.reviewToReviewDto(review);
    }

    @Override
    public void forwardClientAndRent(EmailInfoDto emailInfoDto) {
        List<Rent> rents = rentRepository.findAll();
        Rent rent = null;
        for(Rent r: rents) {
            if(r.getId().equals(emailInfoDto.getRentId())) {
                rent = r;
                break;
            }
        }
        RentClientDto rentClientDto = new RentClientDto();
        rentClientDto.setIncrement(emailInfoDto.isIncrement());
        rentClientDto.setUserId(rent.getUserId());
        rentClientDto.setStartDate(rent.getRentStart());
        rentClientDto.setEndDate(rent.getRentEnd());
        rentClientDto.setCity(rent.getCity());
        rentClientDto.setBusinessName(rent.getBusinessName());
        rentClientDto.setCarType(rent.getCar().getType());
        rentClientDto.setEmail(emailInfoDto.getEmail());
        rentClientDto.setFirstName(emailInfoDto.getFirstName());
        rentClientDto.setLastName(emailInfoDto.getLastName());
        rentClientDto.setManagerEmail(emailInfoDto.getManagerEmail());
        jmsTemplate.convertAndSend(forwardClientRentDestination, messageHelper.createTextMessage(rentClientDto));
    }


    @Override
    public void deleteRent(Long id) {
        Rent rent = rentRepository.findRentById(id);

        LocalDate start = rent.getRentStart();
        LocalDate end = rent.getRentEnd();
        long difference = DAYS.between(start, end);

        ClientQueueDto clientQueueDto = new ClientQueueDto();
        clientQueueDto.setUserId(rent.getUserId());
        clientQueueDto.setBusinessName(rent.getBusinessName());
        clientQueueDto.setRentId(rent.getId());
        clientQueueDto.setDaysToIncrement((int) difference);
        clientQueueDto.setIncrement(false);
        jmsTemplate.convertAndSend(rentNumberDestination, messageHelper.createTextMessage(clientQueueDto));

        rentRepository.deleteById(id);
    }

}
