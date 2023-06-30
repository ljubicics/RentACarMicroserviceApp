package com.example.rentacarnotificationservice.controller;

import com.example.rentacarnotificationservice.dto.NotificationCreateDto;
import com.example.rentacarnotificationservice.dto.NotificationDto;
import com.example.rentacarnotificationservice.mapper.NotificationMapper;
import com.example.rentacarnotificationservice.repository.NotificationRepository;
import com.example.rentacarnotificationservice.service.NotificationServiceSpecification;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private NotificationServiceSpecification notificationServiceSpecification;
    private NotificationMapper notificationMapper;
    private NotificationRepository notificationRepository;

    public NotificationController(NotificationServiceSpecification notificationServiceSpecification, NotificationMapper notificationMapper,
                                  NotificationRepository notificationRepository) {
        this.notificationServiceSpecification = notificationServiceSpecification;
        this.notificationMapper = notificationMapper;
        this.notificationRepository = notificationRepository;
    }

    @ApiOperation(value = "Add notification")
    @PostMapping("/saveNotification")
    public ResponseEntity<NotificationDto> saveNotificationType(@RequestBody @Valid
                                                                NotificationCreateDto notificationCreateDto) {
        return new ResponseEntity<>(notificationServiceSpecification.addNotification(notificationCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update notification")
    @PutMapping("/{id}/updateNotification")
    public ResponseEntity<NotificationDto> updateNotification(@PathVariable("id") Long id, @RequestBody @Valid
    NotificationCreateDto notificationCreateDto) {
        return new ResponseEntity<>(notificationServiceSpecification.updateNotification(id, notificationCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete notification")
    @DeleteMapping("/{id}/deleteNotification")
    public ResponseEntity<?> deleteNotification(@PathVariable("id") Long id) {
        notificationServiceSpecification.deleteNotificationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Notification History")
    @GetMapping("/{email}/notificationHistory")
    public ResponseEntity<?> updateNotification(@PathVariable("email") String email) {
        notificationServiceSpecification.notificationHistory(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}