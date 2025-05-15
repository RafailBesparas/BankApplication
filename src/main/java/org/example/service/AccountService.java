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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Service class handling business logic for customer accounts.
 * <p>
 * This class includes account registration, authentication integration,
 * and execution of core banking operations: deposit, withdrawal, and transfer.
 *
 * <b>Security:</b> Implements Spring Security's {@link UserDetailsService} for
 * login and session support.
 *
 * <b>Compliance & Audit:</b> Every financial operation logs a corresponding {@link Transaction}
 * for traceability and is persisted securely.
 *
 * <b>Design:</b> This class orchestrates repository access and enforces business constraints.
 *
 * @author Rafael Besparas
 */

// Marks the class as a Service component and used for the businees Logic
@Service
public class AccountService implements UserDetailsService {

    // Automatically injects the dependencies needed and the Account Repository to perform database operations on accounts
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionEventProducer kafkaProducer;

    // Injects the Transaction repository to store and fetch transaction records
    @Autowired
    private TransactionRepository transactionRepository;

    // Injects the password encoder to hash the password before saving them
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private NotificationService notificationService;

    // Threshold for low balance in an account
    private static final BigDecimal LOW_BALANCE_THRESHOLD = new BigDecimal("100.00");


    /**
     * Loads user credentials and roles for authentication.
     *
     * @param username the username of the customer
     * @return Spring Security-compatible {@link UserDetails} object
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Searches the database for an account using the account username
        AccountModel account = accountRepository.findByUsername(username);

        // If the account username is not found then throw the error User not found
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // If the username found return a new Account Model instance with user Details and a user role
        return new AccountModel(
                account.getUsername(),
                account.getPassword(),
                account.getBalance(),
                account.getTransactions(),
                Collections.singleton(new SimpleGrantedAuthority("USER"))
        );
    }

    public List<Transaction> searchTransactions(AccountModel account, String type, BigDecimal min, BigDecimal max, LocalDateTime from, LocalDateTime to) {
        return transactionRepository.searchTransactions(account, type, min, max, from, to);
    }

    /**
     * Retrieves the full account model by username.
     *
     * @param username the customer's unique login ID
     * @return {@link AccountModel} or {@code null} if not found
     */
    // Fetch an account using the username
    public AccountModel getByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    /**
     * Registers a new account with an initial balance of 0.
     * Password is stored using BCrypt encryption.
     *
     * @param account new account object with username and raw password
     */
    public void register(AccountModel account) {
        // hash the password before storing it in the database
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        // Set the initial balance to zero
        account.setBalance(BigDecimal.ZERO);
        // Save the user to the database
        accountRepository.save(account);
    }

    /**
     * Increases the account's balance by the deposit amount.
     * Also persists a DEPOSIT transaction record.
     *
     * @param account the target account
     * @param amount  the amount to deposit
     */
    public void deposit(AccountModel account, BigDecimal amount) {
        // Add the deposit amount to the current balance
        account.setBalance(account.getBalance().add(amount));
        // Create a transaction record for the deposit
        Transaction tx = new Transaction(amount, "DEPOSIT", LocalDateTime.now(), account);

        kafkaProducer.sendTransactionEvent("Deposit: $" + amount + " by user " + account.getUsername());

        // Save the transaction to the database
        transactionRepository.save(tx);
        // Update the account with the new balance
        accountRepository.save(account);
    }

    /**
     * Decreases the account's balance by the withdrawal amount if sufficient funds exist.
     * Also persists a WITHDRAWAL transaction record.
     *
     * @param account the target account
     * @param amount  the amount to withdraw
     * @throws RuntimeException if balance is insufficient
     */
    public void withdraw(AccountModel account, BigDecimal amount) {

        // If the user does not have enough money throw an error
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds. You have no money at all! Go get some work done.");
        }

        // Subtract the withdrawal amount from the current balance
        account.setBalance(account.getBalance().subtract(amount));
        // Create a transaction record for the withdrawal
        Transaction tx = new Transaction(amount, "WITHDRAWAL", LocalDateTime.now(), account);
        // Save the transaction to the database
        transactionRepository.save(tx);
        // Save the updated balance to the database
        accountRepository.save(account);

        kafkaProducer.sendTransactionEvent("Withdraw: $" + amount + " by user " + account.getUsername());

        if (account.getBalance().compareTo(LOW_BALANCE_THRESHOLD) < 0) {
            notificationService.sendNotification(
                    account,
                    "⚠️ Your account balance is below $" + LOW_BALANCE_THRESHOLD + ". Please consider topping up.",
                    "ACCOUNT",
                    "HIGH"
            );
        }

    }

    /**
     * Transfers funds between two accounts. Logs both IN and OUT transactions.
     *
     * @param senderUsername    source account username
     * @param recipientUsername target account username
     * @param amount            transfer amount
     * @throws RuntimeException if recipient is not found or sender has insufficient funds
     */

    public void transfer(String senderUsername, String recipientUsername, BigDecimal amount) {
        // Look for both sender and recipient account
        AccountModel sender = getByUsername(senderUsername);
        AccountModel recipient = getByUsername(recipientUsername);

        // If the recipient does not exist throw an error
        if (recipient == null) {
            throw new RuntimeException("Recipient does not exist.");
        }

        // If the sender has no money throw an error
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds for transfer.");
        }

        kafkaProducer.sendTransactionEvent("Transfer: $" + amount + " from " + senderUsername + " to " + recipientUsername);

        // Update balances
        sender.setBalance(sender.getBalance().subtract(amount));

        // Send a notification if the user has a low balance threshold
        if (sender.getBalance().compareTo(LOW_BALANCE_THRESHOLD) < 0) {
            notificationService.sendNotification(
                    sender,
                    "⚠️ Your account balance is below $" + LOW_BALANCE_THRESHOLD + " after transfer.",
                    "ACCOUNT",
                    "HIGH"
            );
        }

        // Subtract from the sender and add to the recipient
        recipient.setBalance(recipient.getBalance().add(amount));

        // Save transactions
        Transaction txOut = new Transaction(amount, "Transfer Out to " + recipientUsername, LocalDateTime.now(), sender);
        // Create two transaction records one for the sender and one for the recipient
        Transaction txIn = new Transaction(amount, "Transfer In from " + senderUsername, LocalDateTime.now(), recipient);

        notificationService.sendNotification(sender, "You transferred $" + amount + " to " + recipientUsername, "TRANSACTION", "MEDIUM");
        notificationService.sendNotification(recipient, "You received $" + amount + " from " + senderUsername, "TRANSACTION", "MEDIUM");

        // Save both transaction records
        transactionRepository.save(txOut);
        transactionRepository.save(txIn);

        // Save updated accounts
        accountRepository.save(sender);
        accountRepository.save(recipient);
    }

    /**
     * Retrieves all transaction history linked to an account.
     * Used for dashboard, reporting, and audits.
     *
     * @param account the account in question
     * @return list of transactions
     */

    // Return all transactions for the given account.
    public List<Transaction> getTransactionHistory(AccountModel account) {
        return transactionRepository.findByAccount(account);
    }

}
