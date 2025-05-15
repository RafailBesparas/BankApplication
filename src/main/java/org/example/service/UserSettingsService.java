package org.example.service;

import org.example.model.AccountModel;
import org.example.model.UserSettings;
import org.example.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {

    @Autowired
    private UserSettingsRepository repo;

    public UserSettings getByAccount(AccountModel account) {
        return repo.findByAccount(account);
    }

    public void saveSettings(UserSettings settings) {
        repo.save(settings);
    }

    public UserSettings createDefault(AccountModel account) {
        UserSettings settings = new UserSettings();
        settings.setAccount(account);
        settings.setTwoFactorEnabled(false);
        settings.setTransactionAlerts(true);
        settings.setSecurityAlerts(true);
        settings.setMonthlyStatements(true);
        settings.setPreferredLanguage("en");
        settings.setPreferredCurrency("EUR");
        settings.setTheme("light");
        settings.setTimeZone("UTC");
        settings.setNumberFormat("1,234.56");
        return repo.save(settings);
    }
}