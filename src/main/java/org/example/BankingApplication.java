package org.example;

import org.example.model.AccountModel;
import org.example.model.ClientProfile;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.ClientProfileRepository;
import org.example.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }
 /**
    @Bean
    CommandLineRunner seedDatabase(AccountRepository accountRepo,
                                   ClientProfileRepository profileRepo,
                                   TransactionRepository transactionRepo,
                                   BCryptPasswordEncoder encoder) {
        return args -> {
            if (accountRepo.count() > 0) {
                System.out.println("Database already seeded. Skipping seeding.");
                return;
            }

            for (int i = 1; i <= 1000; i++) {
                String username = "user" + String.format("%04d", i);
                String rawPassword = "pass" + i;
                BigDecimal balance = new BigDecimal(1000 + i);

                AccountModel account = new AccountModel();
                account.setUsername(username);
                account.setPassword(encoder.encode(rawPassword));
                account.setBalance(balance);
                accountRepo.save(account);

                ClientProfile profile = new ClientProfile();
                profile.setFirstName("First" + i);
                profile.setLastName("Last" + i);
                profile.setDateOfBirth(LocalDate.of(1990, 1, (i % 28) + 1));
                profile.setNationalId("NID" + i);
                profile.setAddress("Address " + i);
                profile.setEmail(username + "@bank.com");
                profile.setPhone("100000" + i);
                profile.setEmploymentStatus("Employed");
                profile.setAnnualIncome(30000.00 + i);
                profile.setAccount(account);
                profileRepo.save(profile);

                transactionRepo.save(new Transaction(new BigDecimal("500.00"), "DEPOSIT", LocalDateTime.now(), account));
                transactionRepo.save(new Transaction(new BigDecimal("200.00"), "WITHDRAWAL", LocalDateTime.now(), account));
            }

            System.out.println("Database seeded with 1000 users.");
        };
    }

    */
}
