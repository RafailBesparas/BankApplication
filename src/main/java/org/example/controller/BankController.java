package org.example.controller;

import org.example.model.AccountModel;
import org.example.model.LoanApplication;
import org.example.model.Transaction;
import org.example.service.AccountService;
import org.example.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Controller // Web controller that will show to the user
public class BankController {

    // Injecting the Account service for account related actions
    @Autowired
    private AccountService accountService;

    // Handles the Get request to /login url
    @GetMapping("/login")
    public String login() {
        return "login"; // returns the login thymleaf view
    }

    // Handles again the get request but now it displays the registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new AccountModel()); // add empty account object to bind the form data
        return "register"; // return the register view
    }

    // I need the post mapping to handle the form submission from the registration
    @PostMapping("/register")
    public String register(@ModelAttribute AccountModel account) {
        accountService.register(account); // Register the new account
        return "redirect:/login"; // Redirect to login after the registration
    }

    // Again we need the Get mapping to get the data and load the user dashboard
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails,
                            Model model,
                            @ModelAttribute("lastTransferRecipient") String lastTransferRecipient) {

        AccountModel account = accountService.getByUsername(userDetails.getUsername()); // get current user account

        model.addAttribute("account", account); // Adds account info to the model
        model.addAttribute("transactions", accountService.getTransactionHistory(account)); // add the transaction history

        if (lastTransferRecipient != null && !lastTransferRecipient.isBlank()) {
            model.addAttribute("lastTransferRecipient", lastTransferRecipient); // Add the recipient info to the model if exists
        }

        if (account.getClientProfile() != null) {
            model.addAttribute("profile", account.getClientProfile()); // If the client has created the profile show it
        }

        return "dashboard"; // return the dashboard view
    }

    // Use again the post mapping to get data from the user and handle the deposit
    @PostMapping("/deposit")
    public String deposit(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam BigDecimal amount,
                          @RequestParam String message) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername()); // Get the logged-in user account
        accountService.deposit(account, amount, message); //Perform the deposit operation
        return "redirect:/dashboard"; // Redirect to the dashboard
    }

    // Handle the withdrawal operation getting data from the user
    @PostMapping("/withdraw")
    public String withdraw(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam BigDecimal amount,
                           @RequestParam String message,
                           RedirectAttributes redirectAttributes) {
        // Find the user account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        try {
            accountService.withdraw(account, amount, message); // perform the withdraw operation
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // if there is an error throw and error
        }
        return "redirect:/dashboard"; // redirect to the dashboard
    }

    // Handle the transfer operation, get data from the user
    @PostMapping("/transfer")
    public String transfer(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam String recipient,
                           @RequestParam BigDecimal amount,
                           @RequestParam String message,
                           RedirectAttributes redirectAttributes) {
        try {
            accountService.transfer(userDetails.getUsername(), recipient, amount, message); // Perform the transfer operation
            redirectAttributes.addFlashAttribute("lastTransferRecipient", recipient); // store to the model the last recipient info
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // throw an error message
        }
        return "redirect:/dashboard"; // redirect to the dashboard when the transaction is complete
    }

    //////////////////// This has to be moved to the TransactionsController
    // Get mapping in order to Filter and search the transactions
    @GetMapping("/transactions")
    public String viewFilteredTransactions(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam(required = false) String type,
                                           @RequestParam(required = false) BigDecimal min,
                                           @RequestParam(required = false) BigDecimal max,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                                           Model model) {
        // Get the logged in users account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Apply the filters to the list of the transactions
        List<Transaction> results = accountService.searchTransactions(account, type, min, max, from, to);
        // Show the filters results to the user
        model.addAttribute("transactions", results);
        return "transaction"; // return the transaction page
    }

    // Handles the get in order to show the notifications
    @GetMapping("/notification")
    public String notificationPage() {
        return "notification"; // Show the notifications page
    }

    // This also has to go in a separate controller

    // Base path for the loans
    @Controller
    @RequestMapping("/loan")
    public class LoanController {

        // From the loan service inject the loan related logic
        @Autowired
        private LoanService loanService;

        // Handle the apply to show the loan form to the user
        @GetMapping("/apply")
        public String showForm(Model model) {
            model.addAttribute("loanApplication", new LoanApplication()); // Binds the form data to a loan object
            return "loan-apply"; // redirect to the loan apply page
        }

        // Get data from the user in application form, Handle the Apply function
        @PostMapping("/apply")
        public String apply(@AuthenticationPrincipal UserDetails userDetails,
                            @ModelAttribute LoanApplication form,
                            @RequestParam("document") MultipartFile file) {

            AccountModel user = accountService.getByUsername(userDetails.getUsername()); // Find the user and its account
            loanService.applyForLoan(user, form, file); // Submit load application and the supporting documents
            return "redirect:/loan/status"; // redirect to the loan status view
        }

        // Handle the loan status, show to the user the status of the loan application
        @GetMapping("/status")
        public String status(@AuthenticationPrincipal UserDetails userDetails, Model model) {
            AccountModel user = accountService.getByUsername(userDetails.getUsername());
            model.addAttribute("loans", loanService.getLoansByUser(user)); // Display the users loan application
            return "loan-status"; // View for loan status
        }
    }
}
