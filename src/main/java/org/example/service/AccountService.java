package org.example.service;

import org.example.model.AccountModel;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    public List<Transaction> getTransactionHistory(AccountModel account){
        return transactionRepository.findByAccount(account.getId());
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountModel account = findAccountByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException("This User does not exist!!! Username or password is incorrect");
        }
        return new AccountModel(
                account.getUsername(),
                account.getPassword(),
                account.getBalance(),
                account.getTransactions(),
                authorities()
        );
    }

    public Collection<? extends GrantedAuthority> authorities(){
        return Arrays.asList(new SimpleGrantedAuthority("User"));
    }

    public void transferAmount(AccountModel fromAccount, String toUsername, BigDecimal amount){
        if(fromAccount.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Not enough money!!! You cannot transfer!!! ");
        }

        AccountModel toAccount = accountRepository.findByUsername(toUsername)
                .orElseThrow(() -> new RuntimeException("Recipient account not found  "));

        // When the account is found then we must deduct the amount from the account
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        //Transfer the money to the other account
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Create transaction records for debit
        Transaction debitTransaction = new Transaction(
                "Transfer Out to" + toAccount.getUsername(),
                amount,
                LocalDateTime.now(),
                fromAccount
        );
        transactionRepository.save(debitTransaction);

        // Create transaction records for Credit
        Transaction creditTransaction = new Transaction(
                "Transfer In to" + fromAccount.getUsername(),
                amount,
                LocalDateTime.now(),
                toAccount
        );
        transactionRepository.save(creditTransaction);

    }

}
