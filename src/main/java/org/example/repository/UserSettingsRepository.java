package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    UserSettings findByAccount(AccountModel account);
}