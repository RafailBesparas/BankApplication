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

@Service
public class LoanService {

    @Autowired
    private LoanApplicationRepository loanRepo;
    @Autowired private RepaymentScheduleRepository scheduleRepo;

    public List<LoanApplication> getLoansByUser(AccountModel user) {
        return loanRepo.findByApplicant(user);
    }

    public LoanApplication applyForLoan(AccountModel applicant, BigDecimal amount, int term, String purpose, String documentPath) {
        LoanApplication loan = new LoanApplication();
        loan.setApplicant(applicant);
        loan.setAmount(amount);
        loan.setTermMonths(term);
        loan.setPurpose(purpose);
        loan.setStatus("PENDING");
        loan.setApplicationDate(LocalDate.now());
        loan.setDocumentPath(documentPath);
        return loanRepo.save(loan);
    }

    public void approveLoan(Long loanId, String admin, Double interestRate, String notes) {
        LoanApplication loan = loanRepo.findById(loanId).orElseThrow();
        loan.setStatus("APPROVED");
        loan.setApprovedBy(admin);
        loan.setInterestRate(interestRate);
        loan.setNotes(notes);

        List<RepaymentSchedule> schedule = generateSchedule(loan);
        loan.setRepaymentSchedules(schedule);
        loanRepo.save(loan);
    }

    private List<RepaymentSchedule> generateSchedule(LoanApplication loan) {
        BigDecimal monthlyRate = BigDecimal.valueOf(loan.getInterestRate() / 100 / 12);
        int n = loan.getTermMonths();
        BigDecimal principal = loan.getAmount();
        BigDecimal monthlyPayment = principal.multiply(monthlyRate)
                .divide(BigDecimal.ONE.subtract((BigDecimal.ONE.add(monthlyRate)).pow(-n, MathContext.DECIMAL64)), MathContext.DECIMAL64);

        List<RepaymentSchedule> schedule = new ArrayList<>();
        BigDecimal remaining = principal;

        for (int i = 1; i <= n; i++) {
            BigDecimal interest = remaining.multiply(monthlyRate);
            BigDecimal principalPart = monthlyPayment.subtract(interest);
            remaining = remaining.subtract(principalPart);

            RepaymentSchedule rs = new RepaymentSchedule();
            rs.setLoan(loan);
            rs.setDueDate(LocalDate.now().plusMonths(i));
            rs.setInterest(interest);
            rs.setPrincipal(principalPart);
            rs.setRemainingBalance(remaining);
            rs.setPaid(false);
            schedule.add(rs);
        }

        return scheduleRepo.saveAll(schedule);
    }
}