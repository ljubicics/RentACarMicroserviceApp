package com.example.rentacaruserservice.repository;

import com.example.rentacaruserservice.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findUserByUsernameAndPassword(String username, String password);

    Manager findManagerByBusinessName(String businessName);
}
