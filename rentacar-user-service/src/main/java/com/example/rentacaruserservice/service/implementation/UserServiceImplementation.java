package com.example.rentacaruserservice.service.implementation;

import com.example.rentacaruserservice.domain.*;
import com.example.rentacaruserservice.dto.*;
import com.example.rentacaruserservice.exception.NotFoundException;
import com.example.rentacaruserservice.listener.helper.MessageHelper;
import com.example.rentacaruserservice.mapper.UserMapper;
import com.example.rentacaruserservice.repository.ClientRepository;
import com.example.rentacaruserservice.repository.ClientStatusRepository;
import com.example.rentacaruserservice.repository.ManagerRepository;
import com.example.rentacaruserservice.repository.UserRepository;
import com.example.rentacaruserservice.security.service.TokenServiceSpecification;
import com.example.rentacaruserservice.service.UserServiceSpecification;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImplementation implements UserServiceSpecification {

    private TokenServiceSpecification tokenServiceSpecification;
    private JmsTemplate jmsTemplate;
    private ClientStatusRepository clientStatusRepository;
    private UserRepository userRepository;
    private ManagerRepository managerRepository;
    private ClientRepository clientRepository;
    private UserMapper userMapper;
    private String findEmailDestination;
    private MessageHelper messageHelper;
    private String clientRegisterDestination;
    private String resetPasswordDestination;

    public UserServiceImplementation(TokenServiceSpecification tokenServiceSpecification, UserRepository userRepository, UserMapper userMapper, ClientRepository clientRepository,
                                     ClientStatusRepository clientStatusRepository, ManagerRepository managerRepository,
                                     @Value("${destination.findEmail}") String findEmailDestination, MessageHelper messageHelper,
                                     JmsTemplate jmsTemplate, @Value("${destination.registerClient}") String clientRegisterDestination,
                                     @Value("${destination.resetPassword}") String resetPasswordDestination) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.userMapper = userMapper;
        this.tokenServiceSpecification = tokenServiceSpecification;
        this.clientStatusRepository = clientStatusRepository;
        this.managerRepository = managerRepository;
        this.findEmailDestination = findEmailDestination;
        this.messageHelper = messageHelper;
        this.jmsTemplate = jmsTemplate;
        this.clientRegisterDestination = clientRegisterDestination;
        this.resetPasswordDestination = resetPasswordDestination;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::userToUserDto);
    }

    @Override
    public Page<ClientDto> findAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable).map(userMapper::clientToClientDto);
    }

    @Override
    public UserDto createClient(ClientCreateDto clientCreateDto) {
        Client client = userMapper.clientCreateDtoToClient(clientCreateDto);
        userRepository.save(client);
        ClientDto clientDto = userMapper.clientToClientDto(client);
        jmsTemplate.convertAndSend(clientRegisterDestination, messageHelper.createTextMessage(clientDto));
        return userMapper.userToUserDto(client);
    }

    @Override
    public UserDto createManager(ManagerCreateDto managerCreateDto) {
        Manager manager = userMapper.managerCreateDtoToManager(managerCreateDto);
        userRepository.save(manager);
        ManagerDto managerDto = userMapper.managerToManagerDto(manager);
        jmsTemplate.convertAndSend(clientRegisterDestination, messageHelper.createTextMessage(managerDto));
        return userMapper.userToUserDto(manager);
    }

    @Override
    public ClientDto updateClient(Long id, ClientCreateDto clientCreateDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Client with id: %d not found.", id)));
        //Set values to product
        client.setUsername(clientCreateDto.getUsername());
        client.setEmail(clientCreateDto.getEmail());
        client.setFirstName(clientCreateDto.getFirstName());
        client.setLastName(clientCreateDto.getLastName());
        client.setPassword(clientCreateDto.getPassword());
        client.setPassportNumber(clientCreateDto.getPassportNumber());
        //Map product to DTO and return it
        return userMapper.clientToClientDto(userRepository.save(client));
    }

    @Override
    public ClientStatusDto findDiscount(Long id) {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String
                            .format("User with id: %d not found.", id)));
            List<ClientStatus> clientStatusList = clientStatusRepository.findAll();

            ClientStatus status = clientStatusList.stream()
                    .filter(clientStatus -> clientStatus.getMaxNumberOfDaysRented() >= client.getNumberOfDaysRented()
                            && clientStatus.getMinNumberOfDaysRented() <= client.getNumberOfDaysRented())
                    .findAny()
                    .get();
            return new ClientStatusDto(status.getDiscount(), status.getRank());
    }

    @Override
    public UserDto banUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id: %d not found.", id)));

        user.setBanned(true);

        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto unbanUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id: %d not found.", id)));

        user.setBanned(false);

        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));
        //Create token payload
        if (user.isBanned() == false) {
            Claims claims = Jwts.claims();
            claims.put("id", user.getId());
            claims.put("role", user.getRole().getName());

            // Generate token
            return new TokenResponseDto(tokenServiceSpecification.generate(claims));
        } else return new TokenResponseDto();

    }

    @Override
    public void changeNumberOfDaysRented(ClientQueueDto clientQueueDto) {
        Client client = clientRepository.findById(clientQueueDto.getUserId())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with that id is not found")));

        if (clientQueueDto.isIncrement()) client.setNumberOfDaysRented(client.getNumberOfDaysRented() + clientQueueDto.getDaysToIncrement());
        else client.setNumberOfDaysRented(client.getNumberOfDaysRented() - clientQueueDto.getDaysToIncrement());
        clientRepository.save(client);


        Manager manager = managerRepository.findManagerByBusinessName(clientQueueDto.getBusinessName());

        EmailInfoDto emailInfoDto = new EmailInfoDto();
        emailInfoDto.setRentId(clientQueueDto.getRentId());
        emailInfoDto.setBusinessName(clientQueueDto.getBusinessName());
        User user = userRepository.findUserById(clientQueueDto.getUserId());
        emailInfoDto.setUserId(user.getId());
        emailInfoDto.setEmail(user.getEmail());
        emailInfoDto.setFirstName(user.getFirstName());
        emailInfoDto.setLastName(user.getLastName());
        emailInfoDto.setManagerEmail(manager.getEmail());
        emailInfoDto.setManagerId(manager.getId());
        emailInfoDto.setIncrement(clientQueueDto.isIncrement());
        // messageQueueDto
        jmsTemplate.convertAndSend(findEmailDestination, messageHelper.createTextMessage(emailInfoDto));
    }

    @Override
    public UserDto updatePassword(Long id, PasswordUserDto passwordUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Client with id: %d not found.", id)));
        user.setPassword(passwordUserDto.getPassword());

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        jmsTemplate.convertAndSend(resetPasswordDestination, messageHelper.createTextMessage(userDto));

        return userMapper.userToUserDto(userRepository.save(user));
    }
}
