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

/**
 * This controller handles notification-related actions like:
 * - Viewing all notifications
 * - Marking one as read
 * - Marking all as read
 */

// Use the rest controller to handle the notification related actions and return JSON data
    // - View all notification
    // - Marking one as read
    // - Marking all as read
@RestController // this returns JSON data instead of views
@RequestMapping("/notifications") // endpoint /notifications
public class NotificationController {

    // Fetch the current users account
    @Autowired
    private AccountService accountService;

    // Inject the Notifications service to handle notification business logic
    @Autowired
    private NotificationService notificationService;

    // Use the get logic to get notifications for the logged in user
    @GetMapping
    public List<Notification> getNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        // Find the account of the current user
        AccountModel user = accountService.getByUsername(userDetails.getUsername());

        // Return all notifications for this user
        return notificationService.getUserNotifications(user);
    }

    // Use the  mapping in order to mark a single notification as read based on its id and user choice
    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    // Use mapping to mark all notifications as read for this current user
    @PatchMapping("/read-all")
    public void markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        AccountModel user = accountService.getByUsername(userDetails.getUsername());
        notificationService.markAllAsRead(user);
    }
}
