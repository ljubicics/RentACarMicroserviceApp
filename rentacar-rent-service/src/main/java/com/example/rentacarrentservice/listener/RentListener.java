package com.example.rentacarrentservice.listener;

import com.example.rentacarrentservice.service.RentServiceSpecification;
import com.example.rentacarrentservice.listener.helper.MessageHelper;
import com.example.rentacarrentservice.userservice.dto.EmailInfoDto;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class RentListener {

    private MessageHelper messageHelper;
    private RentServiceSpecification rentServiceSpecification;
    public RentListener(MessageHelper messageHelper, RentServiceSpecification rentServiceSpecification) {
        this.messageHelper = messageHelper;
        this.rentServiceSpecification = rentServiceSpecification;
    }

    @JmsListener(destination = "${destination.findEmail}", concurrency = "5-10")
    public void forwardClientAndBooking(Message message) throws JMSException {
        EmailInfoDto emailInfoDto = messageHelper.getMessage(message, EmailInfoDto.class);
        rentServiceSpecification.forwardClientAndRent(emailInfoDto);
    }
}