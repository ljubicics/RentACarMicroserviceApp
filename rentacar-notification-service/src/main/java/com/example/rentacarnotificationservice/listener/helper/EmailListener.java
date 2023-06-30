package com.example.rentacarnotificationservice.listener.helper;

import com.example.rentacarnotificationservice.dto.UserDto;
import com.example.rentacarnotificationservice.listener.MessageHelper;
import com.example.rentacarnotificationservice.service.EmailService;
import com.example.rentacarnotificationservice.service.NotificationServiceSpecification;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.rentacarnotificationservice.dto.RentClientDto;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class EmailListener {

    private MessageHelper messageHelper;
    private EmailService emailServiceSpecification;
    private NotificationServiceSpecification notificationServiceSpecification;

    public EmailListener(MessageHelper messageHelper, EmailService emailServiceSpecification,
                         NotificationServiceSpecification notificationServiceSpecification) {
        this.messageHelper = messageHelper;
        this.emailServiceSpecification = emailServiceSpecification;
        this.notificationServiceSpecification = notificationServiceSpecification;
    }

    @JmsListener(destination = "${destination.registerClient}", concurrency = "5-10")
    public void registerClient(Message message) throws JMSException {
        UserDto userDto = messageHelper.getMessage(message, UserDto.class);
        System.out.println(userDto.toString());
        notificationServiceSpecification.sendVerificationMail(userDto,"register");
    }

    @JmsListener(destination = "${destination.forwardClientRent}", concurrency = "5-10")
    public void reservationNotification(Message message) throws JMSException {
        RentClientDto rentClientDto = messageHelper.getMessage(message, RentClientDto.class);
        if (rentClientDto.getIncrement()) {
            notificationServiceSpecification.sendRentMail(rentClientDto,"rent");
        } else notificationServiceSpecification.sendRentMail(rentClientDto,"cancelRent");
    }

    @JmsListener(destination = "${destination.resetPassword}", concurrency = "5-10")
    public void resetPasswordNotification(Message message) throws JMSException {
        UserDto userDto = messageHelper.getMessage(message, UserDto.class);
        notificationServiceSpecification.sendResetPasswordMail(userDto, "reset");
    }

}
