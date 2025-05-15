package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByApplicant(AccountModel account);
    List<LoanApplication> findByStatus(String status);
}
