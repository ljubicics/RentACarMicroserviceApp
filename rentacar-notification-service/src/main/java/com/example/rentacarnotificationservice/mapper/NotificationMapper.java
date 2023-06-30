package com.example.rentacarnotificationservice.mapper;

import com.example.rentacarnotificationservice.domain.Notification;
import com.example.rentacarnotificationservice.dto.NotificationCreateDto;
import com.example.rentacarnotificationservice.dto.NotificationDto;
import com.example.rentacarnotificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    private NotificationRepository notificationRepository;

    public NotificationMapper(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationDto notificationToNotificationDto(Notification notification){
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage(notification.getMessage());
        notificationDto.setName(notification.getName());
        notificationDto.setManagerMassage(notification.getManagerMessage());
        return notificationDto;
    }

    public Notification notificationCreateDtoToNotification(NotificationCreateDto notificationCreateDto){

        Notification notificationType=new Notification();
        notificationType.setMessage(notificationCreateDto.getMessage());
        notificationType.setName(notificationCreateDto.getName());
        notificationType.setManagerMessage(notificationCreateDto.getManagerMessage());
        return notificationType;
    }




}