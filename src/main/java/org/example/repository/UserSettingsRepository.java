package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and managing UserSettings entities.
 * Provides built-in CRUD operations and a custom query for account-specific settings.
 */
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    /**
     * Retrieves the UserSettings associated with a specific account.
     *
     * @param account the account for which settings are to be fetched
     * @return the UserSettings object linked to the given account
     */
    UserSettings findByAccount(AccountModel account);
}