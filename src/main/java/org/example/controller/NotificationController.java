package org.example.controller;

import org.example.model.AccountModel;
import org.example.model.Notification;
import org.example.service.AccountService;
import org.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired private AccountService accountService;
    @Autowired private NotificationService notificationService;

    @GetMapping
    public List<Notification> getNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        AccountModel user = accountService.getByUsername(userDetails.getUsername());
        return notificationService.getUserNotifications(user);
    }

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    @PatchMapping("/read-all")
    public void markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        AccountModel user = accountService.getByUsername(userDetails.getUsername());
        notificationService.markAllAsRead(user);
    }
}