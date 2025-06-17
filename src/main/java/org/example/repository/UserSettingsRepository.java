package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

// Interface used for accessing and managing UserSettings entities
    // Build int Crud operations for a custom query for account specific settings
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    // Retrieves all UserSettings associated with a specific account
    UserSettings findByAccount(AccountModel account);
}