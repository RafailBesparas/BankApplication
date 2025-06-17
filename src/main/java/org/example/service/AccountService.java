package org.example.service;

import org.example.kafka.TransactionEventProducer;
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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

// Class that contain the business logic for User Accounts deposits, withdraws, transfers
@Service // Marks this class as a Spring service component.
public class AccountService implements UserDetailsService { // Implements Spring Security's user detail loading logic.

    // Injects the account repository to get access to database crud operations
    @Autowired private AccountRepository accountRepository;

    // Injects the transaction repository to get access to a database crud operations
    @Autowired private TransactionRepository transactionRepository;

    //Injects the Kafka event producer in order to subscribe to transactions and then eventually show the message
    @Autowired private TransactionEventProducer kafkaProducer;

    // Injects the notification service in order to have the business logic of notifications
    @Autowired private NotificationService notificationService;

    // Inject the encrypter in order to encrypt the passwords
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager; // Provides direct control over JPA operations.

    // Used to define the lowest balance for alerts
    private static final BigDecimal LOW_BALANCE_THRESHOLD = new BigDecimal("100.00");

    @Override // override the method
    // This method loads the user
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Looking for user: " + username); // used for debugging to check how many users are associated with one account
        AccountModel account = accountRepository.findByUsername(username); // Looks up the user in the database.

        if (account == null) { // if the user is not found
            throw new UsernameNotFoundException("User not found"); // throw an exception
        }

        // Return a new UserDetails-compliant AccountModel with role "USER"
        return new AccountModel(
                account.getUsername(), // get the username
                account.getPassword(), // get the passord
                account.getBalance(), // get the balance
                account.getTransactions(), // get transactions
                Collections.singleton(new SimpleGrantedAuthority("USER")) // Account model with the role USER
        );
    }

    // Get an account by username
    public AccountModel getByUsername(String username) {

        return accountRepository.findByUsername(username); // Use the repository function to query the database
    }

    // Register a new account
    public void register(AccountModel account) {
        account.setPassword(passwordEncoder.encode(account.getPassword())); // Hash the password
        account.setBalance(BigDecimal.ZERO); // Set the starting balance to 0
        accountRepository.save(account); // Save the new account to the database
    }

    // Handles the deposit operations
    public void deposit(AccountModel account, BigDecimal amount, String message) {
        validateMessage(message); // Ensure the message is not empty
        account.setBalance(account.getBalance().add(amount)); // Increase the amount in the balance

        // Create a deposit transactions
        Transaction tx = new Transaction(amount, message, "DEPOSIT", LocalDateTime.now(), account);
        // Message for Kafka notifications
        kafkaProducer.sendTransactionEvent("💰 Deposit of $" + amount + " by " + account.getUsername());

        // Save a transaction
        transactionRepository.save(tx);
        // Save the updated account with the new balance
        accountRepository.save(account);
    }

    // Perform the withdraw operation
    public void withdraw(AccountModel account, BigDecimal amount, String message) {
        validateMessage(message); // Validate message

        // Check if the balance is insufficient
        if (account.getBalance().compareTo(amount) < 0) {
            // Throw an exception if there are insufficient funds
            throw new RuntimeException("Insufficient funds.");
        }

        // Deduct the amount of money from the account
        account.setBalance(account.getBalance().subtract(amount));
        // Create withdrawal transaction
        Transaction tx = new Transaction(amount, message, "WITHDRAWAL", LocalDateTime.now(), account);
        // Create the kafka message for the console
        kafkaProducer.sendTransactionEvent("🏧 Withdrawal of $" + amount + " by " + account.getUsername());

        transactionRepository.save(tx); // Save transaction
        accountRepository.save(account); // Save updated account

        // Check if the balance is low
        if (account.getBalance().compareTo(LOW_BALANCE_THRESHOLD) < 0) {
            // Send notification message and priority
            notificationService.sendNotification(
                    account,
                    "⚠️ Your account balance is below $" + LOW_BALANCE_THRESHOLD + ".",
                    "ACCOUNT",
                    "HIGH"
            );
        }
    }

    @Transactional // make the function transactional because I want it to perform transactions
    // Used to manage transactions declarative, this method runs within the database.
    public void transfer(String senderUsername, String recipientUsername, BigDecimal amount, String message) {
        try {
            validateMessage(message); // ensure the message is valid

            AccountModel sender = getByUsername(senderUsername); // Find the sender
            AccountModel recipient = getByUsername(recipientUsername); // find the recient

            if (recipient == null) throw new RuntimeException("Recipient does not exist."); // if the recipient does not exist throw an error

            if (sender.getBalance().compareTo(amount) < 0) throw new RuntimeException("Insufficient funds for transfer."); // If there are not funds throw an error

            // Subtract from the sender
            sender.setBalance(sender.getBalance().subtract(amount));
            // Add to the recipient
            recipient.setBalance(recipient.getBalance().add(amount));

            //Sender's outgoing transaction.
            Transaction txOut = new Transaction(amount, message, "TRANSFER_OUT", LocalDateTime.now(), sender);
            // Recipient's incoming transaction.
            Transaction txIn = new Transaction(amount, message, "TRANSFER_IN", LocalDateTime.now(), recipient);

            //save the out transaction to the repository
            transactionRepository.save(txOut);
            System.out.println("✅ TX OUT saved: " + txOut.getType() + " - $" + txOut.getAmount()); // log to check if the transaction is logged

            // save the transaction incoming
            transactionRepository.save(txIn);
            System.out.println("✅ TX IN saved: " + txIn.getType() + " - $" + txIn.getAmount()); // log the transaction

            // Save the sender
            accountRepository.save(sender);

            // Save the recipient
            accountRepository.save(recipient);

            // Log the changes in the sender balance
            System.out.println("✅ Saving sender: " + sender.getUsername() + " new balance: " + sender.getBalance());

            // Log the changes in the recipient challenge
            System.out.println("✅ Saving recipient: " + recipient.getUsername() + " new balance: " + recipient.getBalance());

            // Give to the user the Kafka message
            kafkaProducer.sendTransactionEvent("🔁 $" + amount + " transferred from " + senderUsername + " to " + recipientUsername);

            // Add notification for the sender
            notificationService.sendNotification(sender, "You sent $" + amount + " to " + recipientUsername, "TRANSACTION", "MEDIUM");
            // Add notification to the recipient
            notificationService.sendNotification(recipient, "You received $" + amount + " from " + senderUsername, "TRANSACTION", "MEDIUM");

            // Alert if sender's balance is now low
            if (sender.getBalance().compareTo(LOW_BALANCE_THRESHOLD) < 0) {
                notificationService.sendNotification(
                        sender,
                        "⚠️ Your balance is below $" + LOW_BALANCE_THRESHOLD + " after the transfer.",
                        "ACCOUNT",
                        "HIGH"
                );
            }

        } catch (Exception e) { // Catch any exception to ensure rollback
            System.err.println("❌ Exception during transfer: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace
            throw e; // re-throw to trigger rollback
        }
    }

    // Returns all transactions for an account
    public List<Transaction> getTransactionHistory(AccountModel account) {
        return transactionRepository.findByAccount(account);
    }

    // Search all the transactions using the filters
    public List<Transaction> searchTransactions(AccountModel account, String type, BigDecimal min, BigDecimal max, LocalDateTime from, LocalDateTime to) {
        return transactionRepository.searchTransactions(account, type, min, max, from, to);
    }

    // Validates that a message is present
    private void validateMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction message cannot be empty."); // Transaction messages cannot be empty
        }
    }
}
