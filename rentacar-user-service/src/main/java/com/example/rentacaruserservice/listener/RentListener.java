package com.example.rentacaruserservice.listener;

import com.example.rentacaruserservice.dto.ClientQueueDto;
import com.example.rentacaruserservice.listener.helper.MessageHelper;
import com.example.rentacaruserservice.service.UserServiceSpecification;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class RentListener {

    private MessageHelper messageHelper;
    private UserServiceSpecification userServiceSpecification;

    public RentListener(MessageHelper messageHelper, UserServiceSpecification userServiceSpecification) {
        this.messageHelper = messageHelper;
        this.userServiceSpecification = userServiceSpecification;
    }
    @JmsListener(destination = "${destination.rentNumber}", concurrency = "5-10")
    public void incrementNumberOfBooking(Message message) throws JMSException {
        ClientQueueDto clientQueueDto = messageHelper.getMessage(message, ClientQueueDto.class);
        System.out.println(clientQueueDto.toString());
        userServiceSpecification.changeNumberOfDaysRented(clientQueueDto);
    }
}