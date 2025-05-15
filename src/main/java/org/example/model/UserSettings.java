package org.example.model;

import jakarta.persistence.*;

@Entity
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private AccountModel account;

    // Security
    private boolean twoFactorEnabled;

    // Communication
    private boolean transactionAlerts;
    private boolean securityAlerts;
    private boolean monthlyStatements;

    private String preferredLanguage;
    private String preferredCurrency;

    // Interface
    private String theme; // dark / light
    private String timeZone;
    private String numberFormat;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public boolean isTransactionAlerts() {
        return transactionAlerts;
    }

    public void setTransactionAlerts(boolean transactionAlerts) {
        this.transactionAlerts = transactionAlerts;
    }

    public boolean isSecurityAlerts() {
        return securityAlerts;
    }

    public void setSecurityAlerts(boolean securityAlerts) {
        this.securityAlerts = securityAlerts;
    }

    public boolean isMonthlyStatements() {
        return monthlyStatements;
    }

    public void setMonthlyStatements(boolean monthlyStatements) {
        this.monthlyStatements = monthlyStatements;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }
}