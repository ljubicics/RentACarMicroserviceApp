package com.example.rentacarnotificationservice.service.implementation;

import com.example.rentacarnotificationservice.domain.Notification;
import com.example.rentacarnotificationservice.domain.NotificationHistory;
import com.example.rentacarnotificationservice.dto.NotificationCreateDto;
import com.example.rentacarnotificationservice.dto.NotificationDto;
import com.example.rentacarnotificationservice.dto.RentClientDto;
import com.example.rentacarnotificationservice.dto.UserDto;
import com.example.rentacarnotificationservice.mapper.NotificationMapper;
import com.example.rentacarnotificationservice.repository.NotificationHistoryRepository;
import com.example.rentacarnotificationservice.repository.NotificationRepository;
import com.example.rentacarnotificationservice.service.EmailService;
import com.example.rentacarnotificationservice.service.NotificationServiceSpecification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImplementation implements NotificationServiceSpecification {
    private NotificationRepository notificationRepository;
    private NotificationMapper notificationMapper;
    private EmailService emailService;
    private NotificationHistoryRepository notificationHistoryRepository;

    public NotificationServiceImplementation(NotificationRepository notificationRepository, NotificationMapper notificationMapper,
                                   EmailService emailService,
                                   NotificationHistoryRepository notificationHistoryRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.emailService = emailService;
        this.notificationHistoryRepository = notificationHistoryRepository;
    }

    @Override
    public NotificationDto addNotification(NotificationCreateDto notificationCreateDto) {
        Notification notification = notificationMapper.notificationCreateDtoToNotification(notificationCreateDto);
        notificationRepository.save(notification);
        return notificationMapper.notificationToNotificationDto(notification);
    }

    @Override
    public NotificationDto updateNotification(Long id, NotificationCreateDto notificationCreateDto) {
        Notification notification = notificationRepository.findNotificationById(id);
        notification.setMessage(notificationCreateDto.getMessage());
        notification.setName(notificationCreateDto.getName());
        return notificationMapper.notificationToNotificationDto(notificationRepository.save(notification));
    }

    @Override
    public void deleteNotificationById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void sendVerificationMail(UserDto userDto, String notificationName) {
        Notification notification = notificationRepository.findNotificationByName(notificationName);
        String content = notification.getMessage();

        String verifyURL = "http://localhost:8081/verify?token=" + userDto.getVerificationCode();
        content = content.replace("%link", verifyURL);

        emailService.sendSimpleMessage(userDto.getEmail(), notificationName, content);

        NotificationHistory notificationHistory = new NotificationHistory(userDto.getEmail(), content, notificationName, userDto.getSendTo());
        notificationHistoryRepository.save(notificationHistory);
    }

    @Override
    public void sendRentMail(RentClientDto rentClientDto, String notificationName) {
        Notification notification = notificationRepository.findNotificationByName(notificationName);
        String content = notification.getMessage();
        String managerContent = notification.getManagerMessage();


        content = content.replace("%firstname", rentClientDto.getFirstName());
        content = content.replace("%lastname", rentClientDto.getLastName());
        content = content.replace("%email", rentClientDto.getEmail());
        content = content.replace("%startDate", rentClientDto.getStartDate().toString());
        content = content.replace("%endDate", rentClientDto.getEndDate().toString());
        content = content.replace("%city", rentClientDto.getCity());
        content = content.replace("%businessName", rentClientDto.getBusinessName());
        content = content.replace("%carType", rentClientDto.getCarType());
        content = content.replace("%email", rentClientDto.getManagerEmail());

        managerContent = managerContent.replace("%userId", String.valueOf(rentClientDto.getUserId()));

        System.out.println(rentClientDto);
        emailService.sendSimpleMessage(rentClientDto.getEmail(), notification.getName(), content);
        emailService.sendSimpleMessage(rentClientDto.getManagerEmail(), "New Rent", managerContent);

        NotificationHistory notificationHistory = new NotificationHistory();
        notificationHistory.setEmail(rentClientDto.getEmail());
        notificationHistory.setRentStart(rentClientDto.getStartDate());
        notificationHistory.setNotificationName(notificationName);
        notificationHistory.setMessage(content);
        notificationHistory.setSendTo("client");

        if (notificationName.equals("cancelReservation")) notificationHistory.setFlag(1);
        else notificationHistory.setFlag(0);
        notificationHistoryRepository.save(notificationHistory);

        NotificationHistory notificationHistoryManager = new NotificationHistory();
        notificationHistoryManager.setEmail(rentClientDto.getManagerEmail());
        notificationHistoryManager.setNotificationName(notificationName);
        notificationHistoryManager.setMessage(managerContent);
        notificationHistoryManager.setFlag(1);
        notificationHistoryManager.setSendTo("manager");

        notificationHistoryRepository.save(notificationHistoryManager);

    }


    @Override
    public void sendReservationReminder(NotificationHistory notificationHistory) {
        emailService.sendSimpleMessage(notificationHistory.getEmail(), "reservation reminder", notificationHistory.getMessage());
        notificationHistory.setFlag(1);
        notificationHistoryRepository.save(notificationHistory);

        NotificationHistory reminder = new NotificationHistory();
        reminder.setEmail(notificationHistory.getEmail());
        reminder.setRentStart(notificationHistory.getRentStart());
        reminder.setNotificationName("reservation reminder");
        reminder.setMessage(notificationHistory.getMessage());
        reminder.setFlag(1);

        notificationHistoryRepository.save(reminder);
    }

    @Override
    public void sendResetPasswordMail(UserDto userDto, String notificationName) {
        Notification notification = notificationRepository.findNotificationByName(notificationName);
        String content = notification.getMessage();
        content = content.replace("%username", userDto.getUsername());
        content = content.replace("%email", userDto.getEmail());
        emailService.sendSimpleMessage(userDto.getEmail(), notificationName, content);
        NotificationHistory notificationHistory = new NotificationHistory(userDto.getEmail(), content, notificationName, userDto.getSendTo());
        notificationHistoryRepository.save(notificationHistory);
    }

    @Override
    public void notificationHistory(String email) {
        List<NotificationHistory> notificationHistory = notificationHistoryRepository.findNotificationHistoryByEmail(email);
        for (NotificationHistory history : notificationHistory) {
            System.out.println(history.getMessage());
        }
    }
}
