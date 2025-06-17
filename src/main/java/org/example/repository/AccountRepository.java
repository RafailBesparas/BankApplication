package org.example.repository;

import org.example.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

// Implementable interface to communicate with the database and manage accounts
    // Allow to save, find, update and delete account records
public interface AccountRepository extends JpaRepository<AccountModel, Long> {

    // Finds the user account by the username, mainly user during login and user lookup
    AccountModel findByUsername(String username);
}
