package com.example.rentacaruserservice.repository;

import com.example.rentacaruserservice.domain.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientStatusRepository extends JpaRepository<ClientStatus, Long> {
}
