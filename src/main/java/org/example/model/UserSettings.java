package org.example.model;

import jakarta.persistence.*;

/**
 * This class stores settings chosen by the user,
 * like language, alerts, theme, and time zone.
 * Each user has one set of settings.
 */
// Class that stores the settings chosen by the user
    // language, alerts, theme, time zone
    // Each user has one set of settings
@Entity // Marks this as a persistent JPA entity mapped to a database table.
public class UserSettings {

    // Unique ID for this settings entry
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    private Long id;

    // The account this settings record belongs to
    @OneToOne // One account must belong to one user
    @JoinColumn(name = "account_id") // Foreign key in the user_settings table
    private AccountModel account;

    // ====== Security Settings ======
    private boolean twoFactorEnabled;

    // ====== Notification Preferences ======
    private boolean transactionAlerts;
    private boolean securityAlerts;
    private boolean monthlyStatements;

    // ====== General Preferences ======
    private String preferredLanguage;
    private String preferredCurrency;

    // ====== Interface Preferences ======
    private String theme;         // "light" or "dark"
    private String timeZone;      // e.g., "Europe/Berlin"
    private String numberFormat;  // e.g., "1,000.00" or "1.000,00"

    // ===== Getters and Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AccountModel getAccount() { return account; }
    public void setAccount(AccountModel account) { this.account = account; }

    public boolean isTwoFactorEnabled() { return twoFactorEnabled; }
    public void setTwoFactorEnabled(boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }

    public boolean isTransactionAlerts() { return transactionAlerts; }
    public void setTransactionAlerts(boolean transactionAlerts) { this.transactionAlerts = transactionAlerts; }

    public boolean isSecurityAlerts() { return securityAlerts; }
    public void setSecurityAlerts(boolean securityAlerts) { this.securityAlerts = securityAlerts; }

    public boolean isMonthlyStatements() { return monthlyStatements; }
    public void setMonthlyStatements(boolean monthlyStatements) { this.monthlyStatements = monthlyStatements; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }

    public String getPreferredCurrency() { return preferredCurrency; }
    public void setPreferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }

    public String getNumberFormat() { return numberFormat; }
    public void setNumberFormat(String numberFormat) { this.numberFormat = numberFormat; }
}
