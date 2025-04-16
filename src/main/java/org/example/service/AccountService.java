package org.example.service;

import org.example.model.AccountModel;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountModel account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new AccountModel(
                account.getUsername(),
                account.getPassword(),
                account.getBalance(),
                account.getTransactions(),
                Collections.singleton(new SimpleGrantedAuthority("USER"))
        );
    }

    public AccountModel getByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public void register(AccountModel account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
    }

    public void deposit(AccountModel account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        Transaction tx = new Transaction(amount, "DEPOSIT", LocalDateTime.now(), account);
        transactionRepository.save(tx);
        accountRepository.save(account);
    }

    public void withdraw(AccountModel account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds. You have no money at all! Go get some work done.");
        }
        account.setBalance(account.getBalance().subtract(amount));
        Transaction tx = new Transaction(amount, "WITHDRAWAL", LocalDateTime.now(), account);
        transactionRepository.save(tx);
        accountRepository.save(account);
    }

    public List<Transaction> getTransactionHistory(AccountModel account) {
        return transactionRepository.findByAccount(account);
    }
}
