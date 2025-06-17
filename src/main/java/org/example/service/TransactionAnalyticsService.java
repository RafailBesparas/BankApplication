package org.example.service;

import org.example.model.AccountModel;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This service helps analyze how users spend their money.
 * It can show spending trends by month or by transaction type.
 */
// Service that helps analyze how users spend their money
    // Spending trends by month or by transactions
@Service  // Registers this class as a Spring service component.
public class TransactionAnalyticsService {

    // Inject the Transaction repository to be able to do Crud operations in the repository table
    @Autowired
    private TransactionRepository transactionRepository;

    // Get the total spending per month for a user
    // Counts money that went out withdrawals or transfers
    public Map<String, BigDecimal> getMonthlySpending(AccountModel account) {
        List<Transaction> transactions = transactionRepository.findByAccount(account); // Fetch all user's transactions.

        return transactions.stream() // Begin stream processing
                // Keep only transactions where money is going out
                .filter(tx -> isSpendingType(tx.getType()))
                // Group by year and month (like "2025-04")
                .collect(Collectors.groupingBy(
                        tx -> tx.getTimestamp().getYear() + "-" + String.format("%02d", tx.getTimestamp().getMonthValue()),  // Group key: "YYYY-MM"
                        // Map each transaction to its amount, // Reduce amounts per month using summation
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }


    // get the total spending grouped by a type
    public Map<String, BigDecimal> getSpendingByCategory(AccountModel account) {
        List<Transaction> transactions = transactionRepository.findByAccount(account); // Load all user's transactions.

        return transactions.stream() // Begin stream
                .filter(tx -> isSpendingType(tx.getType())) // Keep only withdrawals and transfers out
                .collect(Collectors.groupingBy( // Group by transaction type and sum amounts
                        Transaction::getType, // Use transaction type as the group key
                        Collectors.mapping(Transaction::getAmount,  // Map each to its amount
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)) // Reduce using BigDecimal addition
                ));
    }

    // Helper method to check if the transaction type is spending related
    private boolean isSpendingType(String type) {
        String lower = type.toLowerCase();  // Convert to lowercase to avoid case mismatch
        return lower.contains("withdrawal") || lower.contains("transfer out"); // Check if it's a spending type
    }
}
