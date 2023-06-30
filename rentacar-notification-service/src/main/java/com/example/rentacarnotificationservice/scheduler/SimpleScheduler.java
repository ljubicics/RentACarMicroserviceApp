package com.example.rentacarnotificationservice.scheduler;

import com.example.rentacarnotificationservice.service.NotificationServiceSpecification;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.rentacarnotificationservice.domain.NotificationHistory;
import com.example.rentacarnotificationservice.repository.NotificationHistoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleScheduler{

    private NotificationServiceSpecification notificationServiceSpecification;
    private NotificationHistoryRepository notificationHistoryRepository;

    public SimpleScheduler(NotificationServiceSpecification notificationServiceSpecification, NotificationHistoryRepository notificationHistoryRepository) {
        this.notificationServiceSpecification = notificationServiceSpecification;
        this.notificationHistoryRepository = notificationHistoryRepository;
    }

    @Scheduled(fixedDelay = 600000, initialDelay = 600000)
    public void sendReservationReminder(){
        List<NotificationHistory> list = notificationHistoryRepository.findNotificationReservationHistory();

        for (NotificationHistory nh: list){
            notificationServiceSpecification.sendReservationReminder(nh);
        }
    }

}