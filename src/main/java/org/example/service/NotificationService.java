package org.example.service;

import org.example.model.AccountModel;
import org.example.model.Notification;
import org.example.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class that handles business logic related to notifications.
 * Supports creating, reading, and updating notification states.
 */
@Service // Marks this class as a Spring service component
public class NotificationService {

    // Inject the NotificationRepository to perform database operations
    @Autowired
    private NotificationRepository notificationRepo;

    /**
     * Sends a new notification to a user with message, type, and priority.
     *
     * @param user     the target account for the notification
     * @param message  the content of the notification
     * @param type     type of notification (e.g. SECURITY, TRANSACTION)
     * @param priority importance level (e.g. LOW, MEDIUM, HIGH)
     */
    public void sendNotification(AccountModel user, String message, String type, String priority) {
        // Create a new Notification object
        Notification note = new Notification();
        // Set the target user for the notification
        note.setUser(user);
        // Set the notification message
        note.setMessage(message);
        // Set the type of the notification
        note.setType(type);
        // Set the current time as the timestamp
        note.setTimestamp(LocalDateTime.now());
        // Mark the notification as unread initially
        note.setRead(false);
        // Set the priority level of the notification
        note.setPriority(priority);
        // Save the notification to the database
        notificationRepo.save(note);
    }

    /**
     * Retrieves all notifications for a user, sorted from newest to oldest.
     *
     * @param user the user whose notifications are to be fetched
     * @return a list of notifications for the given user
     */
    public List<Notification> getUserNotifications(AccountModel user) {
        // Fetch all notifications for the user, ordered by timestamp descending
        return notificationRepo.findByUserOrderByTimestampDesc(user);
    }

    /**
     * Marks a single notification as read by its ID.
     *
     * @param id the ID of the notification to mark as read
     */
    public void markAsRead(Long id) {
        // Fetch the notification by ID and update its "read" status if it exists
        notificationRepo.findById(id).ifPresent(n -> {
            n.setRead(true); // mark as read
            notificationRepo.save(n); // Save updated notifications
        });
    }

    /**
     * Marks all notifications for a user as read.
     *
     * @param user the user whose notifications should be updated
     */
    public void markAllAsRead(AccountModel user) {
        // Fetch all notifications for the user
        List<Notification> notes = notificationRepo.findByUserOrderByTimestampDesc(user);
        // Iterate through the list and mark each as read
        for (Notification note : notes) {
            note.setRead(true);
        }
        // Save all modified notifications in a batch
        notificationRepo.saveAll(notes);
    }
}