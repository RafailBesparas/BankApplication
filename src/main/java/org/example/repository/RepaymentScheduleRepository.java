package org.example.repository;

import org.example.model.LoanApplication;
import org.example.model.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


// Implementable interface for performing CRUD operations on RepaymentSchedule entities
public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, Long> { // Extends JpaRepository to inherit basic persistence methods.

    // Retrieves all the repayment schedules associated with a give loan
    // Next step it will be used to calculate with a machine learning algorithm the repayment timeline and each instalment
    List<RepaymentSchedule> findByLoan(LoanApplication loan);
}
