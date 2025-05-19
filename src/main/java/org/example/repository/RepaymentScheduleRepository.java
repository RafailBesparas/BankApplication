package org.example.repository;

import org.example.model.LoanApplication;
import org.example.model.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on RepaymentSchedule entities.
 * Extends JpaRepository to inherit basic persistence methods.
 */
public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, Long> {
    /**
     * Retrieves all repayment schedules associated with a given loan.
     * Useful for displaying or calculating repayment timelines for a specific loan application.
     *
     * @param loan the loan application associated with the repayment schedule
     * @return a list of repayment schedule entries for the loan
     */
    List<RepaymentSchedule> findByLoan(LoanApplication loan);
}
