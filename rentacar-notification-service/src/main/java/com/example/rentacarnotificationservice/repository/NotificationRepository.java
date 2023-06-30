package com.example.rentacarnotificationservice.repository;

import com.example.rentacarnotificationservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Notification findNotificationByName(String name);
    Notification findNotificationById(Long id);

}