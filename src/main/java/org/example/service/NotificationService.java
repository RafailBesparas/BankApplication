package org.example.service;

import org.example.model.AccountModel;
import org.example.model.Notification;
import org.example.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// This service handles the user notifications, it lets me send new alerts, read them and mark them as read
@Service // Registers the class as a service bean for dependency injection
public class NotificationService {

    // Inject the NotificationRepository that we can do CRUD operations for notifications
    @Autowired
    private NotificationRepository notificationRepo;


    // Send a message notification to the user
    public void sendNotification(AccountModel user, String message, String type, String priority) {
        Notification note = new Notification(); // create an empty notification
        note.setUser(user); // add the user
        note.setMessage(message); // add the message
        note.setType(type); // Example: SECURITY, TRANSACTION
        note.setTimestamp(LocalDateTime.now()); // add the time
        note.setRead(false); // New notifications are unread
        note.setPriority(priority); // Example: LOW, MEDIUM, HIGH

        notificationRepo.save(note); // save the notification to the repo
    }

    // Get all notifications for a user from the newest to the oldest
    public List<Notification> getUserNotifications(AccountModel user) {
        return notificationRepo.findByUserOrderByTimestampDesc(user);
    }

    // Mark one notification as read using its ID
    public void markAsRead(Long id) {
        notificationRepo.findById(id).ifPresent(note -> {
            note.setRead(true);
            notificationRepo.save(note);
        });
    }


    // Mark all notification for a user as read
    public void markAllAsRead(AccountModel user) {
        List<Notification> notes = notificationRepo.findByUserOrderByTimestampDesc(user); // find all notifications
        for (Notification note : notes) { // iterate for each notification
            note.setRead(true);
        }
        notificationRepo.saveAll(notes); // save all notifications
    }
}
