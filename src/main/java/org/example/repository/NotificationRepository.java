package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Notification entities.
 * Provides basic CRUD functionality and custom query methods for user-specific notifications.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * Retrieves all notifications for a specific user, ordered by timestamp descending.
     * This allows recent notifications to be shown first (e.g., in a UI Notification Center).
     *
     * @param user the account (user) for whom to fetch notifications
     * @return a list of notifications, most recent first
     */
    List<Notification> findByUserOrderByTimestampDesc(AccountModel user);
}