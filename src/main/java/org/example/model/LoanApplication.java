package org.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel applicant;

    private BigDecimal amount;
    private Integer termMonths;
    private Double interestRate;
    private String purpose;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDate applicationDate;
    private String approvedBy;
    private String notes;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<RepaymentSchedule> repaymentSchedules;

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
