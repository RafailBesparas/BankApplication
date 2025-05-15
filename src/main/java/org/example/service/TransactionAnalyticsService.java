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

@Service
public class TransactionAnalyticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Map<String, BigDecimal> getMonthlySpending(AccountModel account) {
        List<Transaction> transactions = transactionRepository.findByAccount(account);
        return transactions.stream()
                .filter(tx -> tx.getType().toLowerCase().contains("withdrawal") || tx.getType().toLowerCase().contains("transfer out"))
                .collect(Collectors.groupingBy(
                        tx -> tx.getTimestamp().getYear() + "-" + String.format("%02d", tx.getTimestamp().getMonthValue()),
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    public Map<String, BigDecimal> getSpendingByCategory(AccountModel account) {
        List<Transaction> transactions = transactionRepository.findByAccount(account);
        return transactions.stream()
                .filter(tx -> tx.getType().toLowerCase().contains("transfer out") || tx.getType().toLowerCase().contains("withdrawal"))
                .collect(Collectors.groupingBy(
                        Transaction::getType,
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }
}