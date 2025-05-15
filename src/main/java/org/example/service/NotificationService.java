package org.example.service;

import org.example.model.AccountModel;
import org.example.model.Notification;
import org.example.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    public void sendNotification(AccountModel user, String message, String type, String priority) {
        Notification note = new Notification();
        note.setUser(user);
        note.setMessage(message);
        note.setType(type);
        note.setTimestamp(LocalDateTime.now());
        note.setRead(false);
        note.setPriority(priority);
        notificationRepo.save(note);
    }

    public List<Notification> getUserNotifications(AccountModel user) {
        return notificationRepo.findByUserOrderByTimestampDesc(user);
    }

    public void markAsRead(Long id) {
        notificationRepo.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepo.save(n);
        });
    }

    public void markAllAsRead(AccountModel user) {
        List<Notification> notes = notificationRepo.findByUserOrderByTimestampDesc(user);
        for (Notification note : notes) {
            note.setRead(true);
        }
        notificationRepo.saveAll(notes);
    }
}