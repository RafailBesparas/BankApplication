package org.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class stores details about a single repayment for a loan.
 * Each repayment has a due date, amount, and status (paid or not).
 */
// This class stores the details of a user to a single repayment for a loan
    // Each repayment can have a due date, an amount and a status paid or unpaid
@Entity // Marks this class as a JPA entity for database persistence.
public class RepaymentSchedule {

    // Unique ID for each repayment record
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-incremented primary key

    // The loan that this repayment belongs to
    @ManyToOne // One loan can have many repayment installations
    @JoinColumn(name = "loan_id") // Foreign key in DB linking to the loan
    private LoanApplication loan;

    // The date by which this repayment should be made
    private LocalDate dueDate;

    // Amount that reduces the loan principal
    private BigDecimal principal;

    // Amount that goes toward interest
    private BigDecimal interest;

    // Loan balance left after this repayment
    private BigDecimal remainingBalance;

    // True if this repayment has already been made
    private boolean paid;

    // ===== Getters and Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LoanApplication getLoan() { return loan; }
    public void setLoan(LoanApplication loan) { this.loan = loan; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public BigDecimal getPrincipal() { return principal; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }

    public BigDecimal getInterest() { return interest; }
    public void setInterest(BigDecimal interest) { this.interest = interest; }

    public BigDecimal getRemainingBalance() { return remainingBalance; }
    public void setRemainingBalance(BigDecimal remainingBalance) { this.remainingBalance = remainingBalance; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
}
