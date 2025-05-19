package org.example.service;

import org.example.model.AccountModel;
import org.example.model.UserSettings;
import org.example.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class to manage user-specific application settings.
 * Handles retrieval, saving, and initialization of settings.
 */
@Service // Marks this class as a Spring-managed service component
public class UserSettingsService {

    // Injects the UserSettingsRepository for DB operations
    @Autowired
    private UserSettingsRepository repo;

    /**
     * Retrieves the user settings for a given account.
     *
     * @param account the user account
     * @return the associated UserSettings object
     */
    public UserSettings getByAccount(AccountModel account) {
        // Use the repository to fetch settings by account
        return repo.findByAccount(account);
    }

    /**
     * Persists the provided user settings to the database.
     * Can be used to update or insert settings.
     *
     * @param settings the UserSettings object to save
     */
    public void saveSettings(UserSettings settings) {
        // Save the user settings entity
        repo.save(settings);
    }

    /**
     * Creates and saves default settings for a newly registered account.
     *
     * @param account the account for which to create settings
     * @return the persisted UserSettings object with defaults
     */
    public UserSettings createDefault(AccountModel account) {
        // Instantiate a new settings object
        UserSettings settings = new UserSettings();
        // Link the settings to the account
        settings.setAccount(account);
        // Set default values for all configurable fields
        settings.setTwoFactorEnabled(false); // 2FA disabled by default
        settings.setTransactionAlerts(true); // Alerts enabled
        settings.setSecurityAlerts(true); // Security alerts enabled
        settings.setMonthlyStatements(true); // Monthly statements enabled
        settings.setPreferredLanguage("en"); // Default language: English
        settings.setPreferredCurrency("EUR"); // Default currency: Euro
        settings.setTheme("light"); // UI theme: light mode
        settings.setTimeZone("UTC"); // Default time zone
        settings.setNumberFormat("1,234.56");  // Default number formatting

        // Save and return the initialized settings
        return repo.save(settings);
    }
}