package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Implementable interface to communicate with the database and loan table
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> { // Extends JpaRepository to provide full CRUD capabilities (create, read, update, delete)

    // Get all loans belonging to a specific user
    List<LoanApplication> findByAccount(AccountModel account);

    // Get all loans by status (e.g., "PENDING", "APPROVED", "REJECTED")
    List<LoanApplication> findByStatus(String status);
}
