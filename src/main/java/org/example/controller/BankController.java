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

/**
 * Main controller for handling core banking operations such as:
 * registration, login, dashboard rendering, deposits, withdrawals,
 * transfers, and transaction filtering.
 *
 * <p>This controller serves as the entry point for user-facing banking features.
 * It interfaces with the AccountService for core logic and manages state via Spring's Model and Security context.</p>
 *
 * <b>Security:</b> All methods rely on authenticated access and are protected via Spring Security.<br>
 * <b>Compliance:</b> Transactions processed are persisted for auditing and reporting.
 *
 * @author Rafael Besparas
 */

// This marks the class here from the Spring MVC design Patter,  as controller which handles the http requests and reutrns views
@Controller

public class BankController {

    @Autowired
    // Injects the AccountService to handle business logic like register, deposit, withdraw, etc.
    private AccountService accountService;

    // Display the notification Center the feature where the user sees the notifications
    @GetMapping("/notification")
    public String notificationPage() {
        return "notification"; // This resolves to notification.html in /templates
    }

    /**
     * GET login page
     *
     * @return login.html
     */
    // When the user is to /logic page show the login.html page
    @GetMapping("/login")
    public String login() {
        return "login";
    }



    /**
     * GET registration page
     *
     * @param model Spring MVC model
     * @return register.html with blank account object
     */
    // Shows the registration form with an Empty AccountModel for the user to fill information in
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // adds the account to the model
        model.addAttribute("account", new AccountModel());
        return "register";
    }

    /**
     * POST registration handler
     *
     * @param account account model bound from registration form
     * @return redirect to login upon success
     */
    // Handles the form submission to create a new account, then redirects the login.
    @PostMapping("/register")
    public String register(@ModelAttribute AccountModel account) {
        accountService.register(account);
        return "redirect:/login";
    }


    // This is the part where for the logged in user this application shows the dashboard
    //Shows balance, transaction history, last transfer recipient, and optional profile info.
    @GetMapping("/dashboard")
    public String dashboard(
            // Get the current user information
            @AuthenticationPrincipal UserDetails userDetails,
                            // Container to pass the data from the backend - to the HTML
                            Model model,
                            //    // Reads also the last transfer from the recipient to be able to show the last transfer details
                            @ModelAttribute("lastTransferRecipient") String lastTransferRecipient) {

        //Find the account linked with the user
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Add account and transactions to my model
        model.addAttribute("account", account);
        model.addAttribute("transactions", accountService.getTransactionHistory(account));


        // This part of the code with the two
        // Shows the last transfer and the client profile available

        // CHeck if in the transfer there is a recipient and also the name is not blank
        if (lastTransferRecipient != null && !lastTransferRecipient.isBlank()) {
            // Add the last transfer to the model
            model.addAttribute("lastTransferRecipient", lastTransferRecipient);
        }

        // Get the client profile from the user and also add the client profile to the model
        if (account.getClientProfile() != null) {
            model.addAttribute("profile", account.getClientProfile());
        }

        // return the dashboard view
        return "dashboard";
    }

    //Process deposit requests from dashboard
    @PostMapping("/deposit")
    // when the user precess the deposit button take the amount to the balance of the user and go back to the dashboard
    public String deposit(
            // Get from the currently logged user the data
            @AuthenticationPrincipal UserDetails userDetails,
                          // Get the amount from the current logged user
                          @RequestParam BigDecimal amount) {
        // Get the user details from the model in order to update the deposit to the right account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Add the number the user has put using the AccountService created and the method deposit.
        accountService.deposit(account, amount);
        // direct to the dashboard but with the updated sum
        return "redirect:/dashboard";
    }

    //Process withdrawals also handle the overdraft errors.
    @PostMapping("/withdraw")
    // When the user uses the withdrawal form, subtract the money from the account
    // If there is not enough money throw an error
    public String withdraw(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam BigDecimal amount,
                           Model model) {
        // Use the account model to pass the information to the account service and find the user
        // details from the username of the user
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // use the method try and catch
        try {
            // From the account service use the withdraw method and subtract the amount the user wants
            accountService.withdraw(account, amount);
         // throw a runtime error if there is a failure on the withdrawal
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        //redirect to the appropriate dashboard
        return "redirect:/dashboard";
    }

    //View transaction history
    // When the user opens the transactions we also show him the filters in the transaction HTML
    @GetMapping("/transactions")
    public String viewFilteredTransactions(
            // Get from the currently logged user the data
            @AuthenticationPrincipal UserDetails userDetails,

            // Get from the user the type
            @RequestParam(required = false) String type,
            // Get from the user the min
            @RequestParam(required = false) BigDecimal min,
            // Get from the user the max
            @RequestParam(required = false) BigDecimal max,
            // Get from the user the Local Date time
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            // Get from the user the Local date time to
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            // Add the data to the model
            Model model) {

        // Get the current user and link him to his account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Search matching transactions based on the provided filters
        List<Transaction> results = accountService.searchTransactions(account, type, min, max, from, to);
        // Add the results to the model transactions and display them to HTML page
        model.addAttribute("transactions", results);
        // Return the view transactions
        return "transaction";
    }

    /**
     * Transfer funds to another user by username.
     */
    @PostMapping("/transfer")
    // the authenticated principal is used to access an authenticated user directly in the controller methdo
    public String transfer(
            // Get from the currently logged user the data
            @AuthenticationPrincipal UserDetails userDetails,
                           // Get from the user the recipient
                           @RequestParam String recipient,
                           // Get from the user the amount
                           @RequestParam BigDecimal amount,
                           // Send temporary data with also a request to redirect the page
                           RedirectAttributes redirectAttributes) {
        try {
            // Try to transfer the specified amount from the logged-in user to the recipient
            accountService.transfer(userDetails.getUsername(), recipient, amount);
            // If the transaction works remember the recipient name and pass it to the dashboard
            redirectAttributes.addFlashAttribute("lastTransferRecipient", recipient);
        } catch (RuntimeException e) {
            // If something goes wrong (e.g. not enough money),
            // it catches the error and sends the error message to the dashboard instead.
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        // It redirects the user to the dashboard view.
        return "redirect:/dashboard";
    }

    /// LOAN Application ////////////////////// controller embedded in the bank controller

    // It is a controller on its own but I created it here and then I did not want to move it
    @Controller
    // When the user is in the loan page run the code below
    @RequestMapping("/loan")
    // Class that will handle the loans from the user
    public class LoanController {

        // Auto Inject the dependencies needed for the loan service
        @Autowired private LoanService loanService;

        // Get the account data, inject the dependencies from the account service
        @Autowired private AccountService accountService;

        // Display the loan application form to the user
        @GetMapping("/apply")
        public String showForm(Model model) {
            // Add a blank Loan Application object to the model
            model.addAttribute("loanApplication", new LoanApplication());
            // Return the Thymleaf template for a loan application
            return "loan-apply";
        }

        // Handles the submission of the loan form
        // Captures the form fields and an uploaded document, processes the loan request,
        // redirects the user to the loan status page.
        @PostMapping("/apply")
        public String apply(
                // Get the current logged in user details
                @AuthenticationPrincipal UserDetails userDetails,
                            // Fill the form from the provided user input.
                            @ModelAttribute LoanApplication form,
                            // Reads the uploaded file
                            @RequestParam("document") MultipartFile file) {
            // Retrieve the account information of the logged in user
            AccountModel user = accountService.getByUsername(userDetails.getUsername());
            // Save the uploaded file
            String docPath = saveFile(file); // implement file storage
            // Call the loan service to create a new loan application entry
            loanService.applyForLoan(user, form.getAmount(), form.getTermMonths(), form.getPurpose(), docPath);
            return "redirect:/loan/status"; // show the user the page loan-status
        }

        // When the user is in the loan-status page provide this code
        @GetMapping("/status")

        public String status(
                // Get the current logged-in user details
                @AuthenticationPrincipal UserDetails userDetails,
                Model model) {
            //Retrieve the user's account from the username
            AccountModel user = accountService.getByUsername(userDetails.getUsername());
            //Fetch all loans associated with the user
            model.addAttribute("loans", loanService.getLoansByUser(user));
            // Provide the loan-status.html view to the user's loan data
            return "loan-status";
        }

        private String saveFile(MultipartFile file) {
            // Save securely (encrypt path and store encrypted version)
            return "/uploads/" + file.getOriginalFilename(); // example
        }
    }


}
