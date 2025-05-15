package org.example.repository;

import org.example.model.LoanApplication;
import org.example.model.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, Long> {
    List<RepaymentSchedule> findByLoan(LoanApplication loan);
}
