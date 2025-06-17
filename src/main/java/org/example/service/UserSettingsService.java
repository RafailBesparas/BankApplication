package org.example.service;

import org.example.model.AccountModel;
import org.example.model.UserSettings;
import org.example.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// This service manages user settings like language, currency, alerts and themes
@Service
public class UserSettingsService {

    // Inject the UserSettingsRepository in order to add Crud Actions for user settings
    @Autowired
    private UserSettingsRepository repo;

    // Get all saved settings for a user account
    public UserSettings getByAccount(AccountModel account) {
        return repo.findByAccount(account);
    }

    // Save or update the user settings
    public void saveSettings(UserSettings settings) {

        repo.save(settings);
    }

    // Create a default settings for a new user account
    public UserSettings createDefault(AccountModel account) {
        UserSettings settings = new UserSettings();

        settings.setAccount(account);              // Link settings to the user
        settings.setTwoFactorEnabled(false);       // 2FA is off by default
        settings.setTransactionAlerts(true);       // Enable transaction alerts
        settings.setSecurityAlerts(true);          // Enable security alerts
        settings.setMonthlyStatements(true);       // Enable monthly statements
        settings.setPreferredLanguage("en");       // Default language: English
        settings.setPreferredCurrency("EUR");      // Default currency: Euro
        settings.setTheme("light");                // Use light theme
        settings.setTimeZone("UTC");               // Use UTC time zone
        settings.setNumberFormat("1,234.56");      // Default number format

        return repo.save(settings); // Save to database and return
    }
}
