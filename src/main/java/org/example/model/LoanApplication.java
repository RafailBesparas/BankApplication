package org.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * Entity representing a loan application submitted by an account holder.
 * Stores key loan details such as amount, term, interest, status, and repayment plan.
 */
@Entity // Marks this class as a JPA entity for ORM mapping
public class LoanApplication {

    // Primary key for the loan application
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    // Relationship to the applicant (linked account model)
    // One User must have one Account and can have one Account
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel applicant;

    // Requested loan amount
    private BigDecimal amount;

    // Duration of the loan in months
    private Integer termMonths;

    // Interest rate applied to the loan
    private Double interestRate;

    // Purpose of the loan (e.g., Education, Home, Business)
    private String purpose;

    // Application status: PENDING, APPROVED, or REJECTED
    private String status; // PENDING, APPROVED, REJECTED

    // Date of application submission
    private LocalDate applicationDate;

    // Name of the approver (admin or system)
    private String approvedBy;

    // Any notes or comments about the loan decision
    private String notes;

    // Repayment schedule entries associated with this loan
    // One loan can have multiple Repayment Schedules
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<RepaymentSchedule> repaymentSchedules;

    // Optional path to uploaded loan documents (PDF, DOC, etc.)
    private String documentPath; // optional

    // Getters & Setters

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public AccountModel getApplicant() {
        return applicant;
    }

    public void setApplicant(AccountModel applicant) {
        this.applicant = applicant;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<RepaymentSchedule> getRepaymentSchedules() {
        return repaymentSchedules;
    }

    public void setRepaymentSchedules(List<RepaymentSchedule> repaymentSchedules) {
        this.repaymentSchedules = repaymentSchedules;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
}
