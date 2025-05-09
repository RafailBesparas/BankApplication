package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(AccountModel account);
}
