package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations and custom queries
 * on the LoanApplication entity. Extends Spring Data JPA's JpaRepository.
 */
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    /**
     * Finds all loan applications submitted by a specific account holder.
     *
     * @param account the applicant (AccountModel)
     * @return a list of loan applications associated with the account
     */
    List<LoanApplication> findByApplicant(AccountModel account);

    /**
     * Finds all loan applications by their current status.
     * Useful for filtering applications based on workflow (e.g., "PENDING", "APPROVED", "REJECTED").
     *
     * @param status the status of the loan application
     * @return a list of loan applications matching the status
     */
    List<LoanApplication> findByStatus(String status);
}
