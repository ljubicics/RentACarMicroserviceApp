package com.example.rentacaruserservice.service;

import com.example.rentacaruserservice.domain.Manager;
import com.example.rentacaruserservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserServiceSpecification {
    Page<UserDto> findAll(Pageable pageable);
    Page<ClientDto> findAllClients(Pageable pageable);
    UserDto createClient(ClientCreateDto clientCreateDto);
    UserDto createManager(ManagerCreateDto managerCreateDto);
    ClientDto updateClient(Long id, ClientCreateDto clientCreateDto);
    ClientStatusDto findDiscount(Long id);
    UserDto banUser(Long id);
    UserDto unbanUser(Long id);
    TokenResponseDto login(TokenRequestDto tokenRequestDto);
    void changeNumberOfDaysRented(ClientQueueDto clientQueueDto);
    UserDto updatePassword(Long id, PasswordUserDto passwordUserDto);
}
