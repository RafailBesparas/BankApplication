package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * This class represents a notification sent to a user.
 * Examples: a login alert, a deposit alert, or a promotional message.
 */
// This class is a representation of a notification sent to a user
    // Example is a login alert, deposit alert, promotional message
@Entity // Marks this as a JPA entity (mapped to a table in the database).
public class Notification {

    // Unique ID for the notification (auto-generated)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who receives this notification
    @ManyToOne // Many notifications belong to one user
    @JoinColumn(name = "account_id") // Foreign key column in the database
    private AccountModel user;

    // The message shown to the user
    private String message;

    // Type of notification (example: SECURITY, TRANSACTION)
    private String type;

    // When the notification was created
    private LocalDateTime timestamp;

    // Whether the user has already read this notification
    @Column(name = "is_read")
    private boolean read;

    // Notification priority: LOW, MEDIUM, or HIGH
    private String priority;

    // ===== Getters and Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AccountModel getUser() { return user; }
    public void setUser(AccountModel user) { this.user = user; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
