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

// Tells spring to handle HTTP API requests and responses should be JSON
@RestController
//  sets the base path for all endpoints in this controller.
//  So every method here will start with /notifications
@RequestMapping("/notifications")
public class NotificationController {

    // With these two fields I let the controller communicate with my business logic classes
    //@Autowired tells Spring to automatically give you an instance of these classes so you can use their methods
    @Autowired private AccountService accountService;
    @Autowired private NotificationService notificationService;

    // Handles all the get requests to /notifications
    @GetMapping
    // @AuthenticationPrincipal tells Spring to inject the currently logged-in user (the one making the request).
    // Return a list of user notifications
    public List<Notification> getNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        // Use the username to fetch the actual user data from the database
        AccountModel user = accountService.getByUsername(userDetails.getUsername());
        // Ask the NotificationService to get this user's notifications and return them as the response
        return notificationService.getUserNotifications(user);
    }

    // Handles the Patch request
    // @PatchMapping means "make a small change" (in this case, marking one notification as read).
    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        // Tell the service to mark that specific notification as read
        notificationService.markAsRead(id);
    }

    // This handles a PATCH request to /notifications/read-all
    @PatchMapping("/read-all")
    public void markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        AccountModel user = accountService.getByUsername(userDetails.getUsername());
        notificationService.markAllAsRead(user);
    }
}