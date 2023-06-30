package com.example.rentacarnotificationservice.service;

import com.example.rentacarnotificationservice.domain.NotificationHistory;
import com.example.rentacarnotificationservice.dto.*;

public interface NotificationServiceSpecification {

    NotificationDto addNotification(NotificationCreateDto notificationCreateDto);
    void deleteNotificationById(Long id);
    NotificationDto updateNotification(Long id, NotificationCreateDto notificationCreateDto);
    void sendVerificationMail(UserDto userDto, String notificationName);
    void sendRentMail(RentClientDto rentClientDto, String notificationName);
    void sendReservationReminder(NotificationHistory notificationHistory);
    void sendResetPasswordMail(UserDto userDto, String notificationName);
    void notificationHistory(String email);
}