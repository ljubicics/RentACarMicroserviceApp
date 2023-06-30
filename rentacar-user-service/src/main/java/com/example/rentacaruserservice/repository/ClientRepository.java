package com.example.rentacaruserservice.repository;

import com.example.rentacaruserservice.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findUserByUsernameAndPassword(String username, String password);
    Client findUserByUsername(String username);
    Client findUserByEmail(String email);

}