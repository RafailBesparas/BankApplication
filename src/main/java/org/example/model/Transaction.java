package org.example.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String type; // "DEPOSIT" or "WITHDRAWAL"
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel account;

    public Transaction() {}

    public Transaction(BigDecimal amount, String type, LocalDateTime timestamp, AccountModel account) {
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.account = account;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public AccountModel getAccount() { return account; }

    public void setAccount(AccountModel account) { this.account = account; }
}
