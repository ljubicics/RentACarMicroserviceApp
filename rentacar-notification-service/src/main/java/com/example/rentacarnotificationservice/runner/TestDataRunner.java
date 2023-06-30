package com.example.rentacarnotificationservice.runner;

import com.example.rentacarnotificationservice.domain.Notification;
import com.example.rentacarnotificationservice.repository.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {
    private NotificationRepository notificationRepository;

    public TestDataRunner(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Notification notification = new Notification();
        notification.setName("rent");
        notification.setMessage("%firstname, %lastname, %email, %startDate. %endDate, %city, %businessName, %carType, %email");
        notification.setManagerMessage("%userId");

        notificationRepository.save(notification);
    }
}