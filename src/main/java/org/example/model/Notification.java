package org.example.model;

// JPA annotations for ORM mapping
import jakarta.persistence.*;
// Timestamp for notification creation
import java.time.LocalDateTime;

/**
 * Entity representing a notification sent to a user.
 * Notifications can relate to account events, transactions, promotions, or security alerts.
 */
@Entity // Marks this class as a JPA entity for persistence
public class Notification {

    // Primary key for the notification record
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User (account) associated with the notification
    // One user account can be associated with multiple notifications
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel user;

    // Notification message content
    private String message;
    // Type of message
    private String type; // SECURITY, TRANSACTION, ACCOUNT, PROMOTION

    // Time when the notification was created or sent
    private LocalDateTime timestamp;
    // Flag to indicate if the notification has been read
    private boolean read;
    // Priority level of the notification
    private String priority; // LOW, MEDIUM, HIGH

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountModel getUser() {
        return user;
    }

    public void setUser(AccountModel user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}