package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.example.model.AccountModel;

// This class represents a loan application
   // It capture personal information, finances, employement data etc
@Entity // Declares this class as a JPA entity for database persistence.
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier, auto-incremented in DB.

    // ===== Loan Details =====
    private double amount;
    private String purpose;
    private int termMonths;

    // ==== Personal Information ====
    private String fullName;
    private LocalDate dateOfBirth;
    private String nationality;
    private String address;
    private String maritalStatus;
    private int numberOfDependents;

    // ==== Employment and Income =====
    private String employmentStatus;
    private String employerName;
    private int employmentDurationMonths;
    private double monthlyNetIncome;
    private double otherIncome;

    // ==== Financial Obligations ====
    private double monthlyRentOrMortgage;
    private boolean hasExistingLoans;

    // ==== Supporting Document ====
    private String documentPath;

    // ==== Application Metadata ====
    private LocalDate applicationDate = LocalDate.now();
    private String status = "PENDING"; // "APPROVED", "REJECTED"

    // Account Link Many loans can be associated to one account
    @ManyToOne
    @JoinColumn(name = "account_id") // Foreign key reference to AccountModel
    private AccountModel account;

    public LoanApplication() {} // Default constructor required by JPA

    public LoanApplication(double amount, String purpose, int termMonths) {
        this.amount = amount;
        this.purpose = purpose;
        this.termMonths = termMonths;
    }

    // === Getters and Setters ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public int getNumberOfDependents() { return numberOfDependents; }
    public void setNumberOfDependents(int numberOfDependents) { this.numberOfDependents = numberOfDependents; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }

    public int getEmploymentDurationMonths() { return employmentDurationMonths; }
    public void setEmploymentDurationMonths(int employmentDurationMonths) { this.employmentDurationMonths = employmentDurationMonths; }

    public double getMonthlyNetIncome() { return monthlyNetIncome; }
    public void setMonthlyNetIncome(double monthlyNetIncome) { this.monthlyNetIncome = monthlyNetIncome; }

    public double getOtherIncome() { return otherIncome; }
    public void setOtherIncome(double otherIncome) { this.otherIncome = otherIncome; }

    public double getMonthlyRentOrMortgage() { return monthlyRentOrMortgage; }
    public void setMonthlyRentOrMortgage(double monthlyRentOrMortgage) { this.monthlyRentOrMortgage = monthlyRentOrMortgage; }

    public boolean isHasExistingLoans() { return hasExistingLoans; }
    public void setHasExistingLoans(boolean hasExistingLoans) { this.hasExistingLoans = hasExistingLoans; }

    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }

    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public AccountModel getAccount() { return account; }
    public void setAccount(AccountModel account) { this.account = account; }
}
