package com.example.rentacaruserservice.mapper;

import com.example.rentacaruserservice.domain.Client;
import com.example.rentacaruserservice.domain.Manager;
import com.example.rentacaruserservice.domain.User;
import com.example.rentacaruserservice.dto.*;
import com.example.rentacaruserservice.repository.RoleRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class UserMapper {

    private RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setBanned(user.isBanned());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public Client clientCreateDtoToClient(ClientCreateDto clientCreateDto) {
        Client client = new Client();
        client.setEmail(clientCreateDto.getEmail());
        client.setFirstName(clientCreateDto.getFirstName());
        client.setLastName(clientCreateDto.getLastName());
        client.setUsername(clientCreateDto.getUsername());
        client.setPassword(clientCreateDto.getPassword());
        client.setPassportNumber(clientCreateDto.getPassportNumber());
        client.setRole(roleRepository.findRoleByName("ROLE_CLIENT").get());
        client.setNumberOfDaysRented(20);
        client.setBanned(false);
        String randomCode = RandomString.make(64);
        client.setVerificationCode(randomCode);

        return client;
    }

    public ClientDto clientToClientDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setEmail(client.getEmail());
        clientDto.setFirstName(client.getFirstName());
        clientDto.setUsername(client.getUsername());
        clientDto.setLastName(client.getLastName());
        clientDto.setPassportNumber(client.getPassportNumber());
        clientDto.setNumberOfDaysRented(client.getNumberOfDaysRented());
        String randomCode = RandomString.make(64);
        clientDto.setVerificationCode(randomCode);
        return clientDto;
    }

    public Manager managerCreateDtoToManager(ManagerCreateDto managerCreateDto) {
        Manager manager = new Manager();
        manager.setEmail(managerCreateDto.getEmail());
        manager.setFirstName(managerCreateDto.getFirstName());
        manager.setLastName(managerCreateDto.getLastName());
        manager.setUsername(managerCreateDto.getUsername());
        manager.setPassword(managerCreateDto.getPassword());
        manager.setBusinessName(managerCreateDto.getBusinessName());
        manager.setHiringDate(Date.valueOf(LocalDate.now()));
        manager.setRole(roleRepository.findRoleByName("ROLE_MANAGER").get());
        manager.setBanned(false);
        String randomCode = RandomString.make(64);
        manager.setVerificationCode(randomCode);
        return manager;
    }

    public ManagerDto managerToManagerDto(Manager manager) {
        ManagerDto managerDto = new ManagerDto();
        managerDto.setId(manager.getId());
        managerDto.setEmail(manager.getEmail());
        managerDto.setUsername(manager.getUsername());
        managerDto.setFirstName(manager.getFirstName());
        managerDto.setLastName(manager.getLastName());
        String randomCode = RandomString.make(64);
        manager.setVerificationCode(randomCode);

        return managerDto;
    }

}
