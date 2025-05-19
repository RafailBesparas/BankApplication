package org.example.service;

import org.example.model.AccountModel;
import org.example.model.LoanApplication;
import org.example.model.RepaymentSchedule;
import org.example.repository.LoanApplicationRepository;
import org.example.repository.RepaymentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing loan applications and repayment schedules.
 * Handles loan submission, approval, and amortized repayment schedule generation.
 */
@Service // Marks this class as a Spring-managed service component
public class LoanService {

    // Injects the LoanApplicationRepository to interact with loan data
    @Autowired
    private LoanApplicationRepository loanRepo;

    // Injects the RepaymentScheduleRepository to interact with repayment data
    @Autowired private RepaymentScheduleRepository scheduleRepo;

    /**
     * Retrieves all loans associated with a specific user account.
     *
     * @param user The account model representing the user
     * @return List of loan applications submitted by the user
     */
    public List<LoanApplication> getLoansByUser(AccountModel user) {
        // Uses the repository to fetch loans by applicant
        return loanRepo.findByApplicant(user);
    }

    /**
     * Handles a new loan application submission.
     *
     * @param applicant     The user applying for the loan
     * @param amount        The requested loan amount
     * @param term          Loan duration in months
     * @param purpose       Reason for the loan
     * @param documentPath  Optional path to supporting documents
     * @return The saved LoanApplication entity
     */
    public LoanApplication applyForLoan(AccountModel applicant, BigDecimal amount, int term, String purpose, String documentPath) {
        // Create a new loan application instance
        LoanApplication loan = new LoanApplication();
        // Set the applicant (account holder)
        loan.setApplicant(applicant);
        // Set the requested amount
        loan.setAmount(amount);
        // Set the term in months
        loan.setTermMonths(term);
        // Set the loan purpose (e.g., Education, Car, Medical)
        loan.setPurpose(purpose);
        // Set status as pending initially
        loan.setStatus("PENDING");
        // Set the current date as application date
        loan.setApplicationDate(LocalDate.now());
        // Set the optional document path if provided
        loan.setDocumentPath(documentPath);
        // Save the loan and return the persisted object
        return loanRepo.save(loan);
    }

    /**
     * Approves a loan and generates its repayment schedule.
     *
     * @param loanId        The ID of the loan to approve
     * @param admin         The administrator approving the loan
     * @param interestRate  Annual interest rate as a percentage
     * @param notes         Notes from the approver
     */
    public void approveLoan(Long loanId, String admin, Double interestRate, String notes) {
        // Fetch the loan by ID or throw an exception if not found
        LoanApplication loan = loanRepo.findById(loanId).orElseThrow();
        // Update the status to APPROVED
        loan.setStatus("APPROVED");
        // Record who approved the loan
        loan.setApprovedBy(admin);
        // Set the agreed interest rate
        loan.setInterestRate(interestRate);
        // Add any approval notes
        loan.setNotes(notes);

        // Generate the repayment schedule based on loan terms
        List<RepaymentSchedule> schedule = generateSchedule(loan);
        // Assign the generated schedule to the loan
        loan.setRepaymentSchedules(schedule);
        // Save the updated loan back to the database
        loanRepo.save(loan);
    }

    /**
     * Generates a monthly amortized repayment schedule for the approved loan.
     *
     * @param loan The approved LoanApplication
     * @return A list of RepaymentSchedule objects saved to the DB
     */
    private List<RepaymentSchedule> generateSchedule(LoanApplication loan) {
        // Convert annual interest rate to monthly decimal rate
        BigDecimal monthlyRate = BigDecimal.valueOf(loan.getInterestRate() / 100 / 12);
        // Number of total payments (months)
        int n = loan.getTermMonths();
        // Total principal to be repaid
        BigDecimal principal = loan.getAmount();
        // Calculate monthly payment using amortization formula: (Got this formula from I course I did when I was 21 in the university)
        // A = P * r / (1 - (1 + r)^-n)
        BigDecimal monthlyPayment = principal.multiply(monthlyRate)
                .divide(BigDecimal.ONE.subtract((BigDecimal.ONE.add(monthlyRate)).pow(-n, MathContext.DECIMAL64)), MathContext.DECIMAL64);

        // Create an empty list to hold all repayment entries
        List<RepaymentSchedule> schedule = new ArrayList<>();
        // Start with the total amount as the remaining balance
        BigDecimal remaining = principal;

        // Loop through each month to calculate payments
        for (int i = 1; i <= n; i++) {
            // Calculate interest portion for this month
            BigDecimal interest = remaining.multiply(monthlyRate);
            // The rest goes toward principal
            BigDecimal principalPart = monthlyPayment.subtract(interest);
            // Update remaining balance after this payment
            remaining = remaining.subtract(principalPart);

            // Create a new repayment entry
            RepaymentSchedule rs = new RepaymentSchedule();
            // Link this schedule to the loan
            rs.setLoan(loan);
            // Set the due date i months from now
            rs.setDueDate(LocalDate.now().plusMonths(i));
            // Set the interest due for this payment
            rs.setInterest(interest);
            // Set the principal due for this payment
            rs.setPrincipal(principalPart);
            // Set the remaining balance after this payment
            rs.setRemainingBalance(remaining);
            // Mark this payment as unpaid
            rs.setPaid(false);
            // Add this schedule entry to the list
            schedule.add(rs);
        }

        // Save all schedule entries to the database and return them
        return scheduleRepo.saveAll(schedule);
    }
}

///////////////////////// Loan Formula
// A = P * r / (1 - (1 + r)^-n)
// is the standard amortization formula used to calculate fixed monthly payments for a loan
// Formula Breakdown
// A = Monthly payment
// P = Principal loan amount (total borrowed)
// r = Monthly interest rate (annual rate ÷ 12 ÷ 100)
// n = Total number of monthly payments (loan term in months)