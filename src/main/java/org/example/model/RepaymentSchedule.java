package org.example.model;

// JPA annotations for ORM mapping
import jakarta.persistence.*;
// Precise numeric representation for financial values
import java.math.BigDecimal;
// Represents due date of a repayment
import java.time.LocalDate;

/**
 * Entity representing a single repayment entry for a loan.
 * Each entry includes details such as principal, interest, due date,
 * whether it's been paid, and the remaining loan balance.
 */
@Entity
// Marks this class as a persistent JPA entity
public class RepaymentSchedule {

    /** Primary key for the repayment schedule entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many repayment schedule entries can be associated with one loan
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private LoanApplication loan;

    /** Due date of the scheduled repayment */
    private LocalDate dueDate;
    /** Portion of the payment that goes toward reducing principal */
    private BigDecimal principal;
    /** Portion of the payment that goes toward interest */
    private BigDecimal interest;
    /** Remaining balance on the loan after this payment */
    private BigDecimal remainingBalance;
    /** Whether the repayment has been completed or not */
    private boolean paid;

    // Getters & Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanApplication getLoan() {
        return loan;
    }

    public void setLoan(LoanApplication loan) {
        this.loan = loan;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
