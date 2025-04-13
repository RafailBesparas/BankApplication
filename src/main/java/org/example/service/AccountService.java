package org.example.service;

import org.example.model.AccountModel;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public AccountModel findAccountByUsername(String username){
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Please Register! Account Not Found"));
    }

    public AccountModel registerAccount(String username, String password){
        if(accountRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("UserFound!! Username already exists!! Please pick another username");
        }

        AccountModel account = new AccountModel();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    public void deposit(AccountModel account, BigDecimal amount){
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);

            Transaction transaction = new Transaction(
                    "Deposit",
                    amount,
                    LocalDateTime.now(),
                    account
            );
            transactionRepository.save(transaction);
    }

    public void withdraw(AccountModel account, BigDecimal amount){
        if(account.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Insufficient funds. You have no money at all! Go get some work done")
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                "Withdraw",
                amount,
                LocalDateTime.now(),
                account
        );
        transactionRepository.save(transaction);
    }

}
