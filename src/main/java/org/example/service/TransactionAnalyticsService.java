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
 * Service class that provides analytical methods for transaction data.
 * Includes monthly spending trends and spending by category.
 */
@Service
// Marks this class as a Spring-managed service component
public class TransactionAnalyticsService {

    // Inject the TransactionRepository for database access
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Calculates total spending per month for the given account.
     * It filters only "withdrawal" or "transfer out" type transactions.
     *
     * @param account the account to analyze
     * @return a map with keys as "YYYY-MM" and values as total spending amounts
     */
    public Map<String, BigDecimal> getMonthlySpending(AccountModel account) {
        // Fetch all transactions associated with the given account
        List<Transaction> transactions = transactionRepository.findByAccount(account);
        // Process the transactions using a Stream
        return transactions.stream()
                // Filter only withdrawals and outgoing transfers
                .filter(tx -> tx.getType().toLowerCase().contains("withdrawal") || tx.getType().toLowerCase().contains("transfer out"))
                // Group by year-month string like "2025-04"
                .collect(Collectors.groupingBy(
                        tx -> tx.getTimestamp().getYear() + "-" + String.format("%02d", tx.getTimestamp().getMonthValue()),
                        // Aggregate transaction amounts using reducing to sum values
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    /**
     * Calculates total spending by transaction type (category).
     * Only considers types like "withdrawal" and "transfer out".
     *
     * @param account the account to analyze
     * @return a map with transaction types as keys and total spent as values
     */
    public Map<String, BigDecimal> getSpendingByCategory(AccountModel account) {
        // Fetch all transactions for the account
        List<Transaction> transactions = transactionRepository.findByAccount(account);
        // Process and aggregate them by type
        return transactions.stream()
                // Filter only outgoing money flows
                .filter(tx -> tx.getType().toLowerCase().contains("transfer out") || tx.getType().toLowerCase().contains("withdrawal"))
                // Group by the transaction type (category) string
                .collect(Collectors.groupingBy(
                        Transaction::getType,
                        // Sum the amounts in each group
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }
}