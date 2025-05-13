package org.example.controller;

import org.example.model.AccountModel;
import org.example.model.Transaction;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

/**
 * Main controller for handling core banking operations such as:
 * registration, authentication, dashboard access, deposits, withdrawals, transfers, and transaction history.
 *
 * <p>
 * This controller bridges the user interface with backend logic for customer-facing features.
 * All operations are secured via Spring Security and routed based on user authentication status.
 *
 * <b>Security Note:</b> Every route here must be protected via SecurityConfig. Sensitive operations (e.g., transfer)
 * are executed based on authenticated principals only.
 *
 * <b>Audit Note:</b> Transactions are persisted and can be queried per user for history and compliance.
 * </p>
 *
 * @author Rafael Besparas
 */

// This marks the class here from the Spring MVC design Patter,  as controller which handles the http requests and reutrns views
@Controller

public class BankController {

    @Autowired
    // Injects the AccountService to handle business logic like register, deposit, withdraw, etc.
    private AccountService accountService;

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

    /**
     * Main dashboard after login
     *
     * @param userDetails           authenticated user
     * @param model                 Spring model
     * @param lastTransferRecipient optionally injected via flash attribute
     * @return dashboard.html with balance, profile, and recent activity
     */
    // This is the part where for the logged in user this application shows the dashboard
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails,
                            Model model,
                            //    // Reads also the last transfer from the recipient to be able to show the last transfer details
                            @ModelAttribute("lastTransferRecipient") String lastTransferRecipient) {

        //Gets the users account and adds it and the transaction history to the page.
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
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

    /**
     * Process deposit requests from dashboard
     *
     * @param userDetails user performing the deposit
     * @param amount      deposit amount
     * @return redirect to dashboard
     */
    @PostMapping("/deposit")
    // when the user precess the deposit button take the amount to the balance of the user and go back to the dashboard
    public String deposit(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam BigDecimal amount) {
        // Get the user details from the model in order to update the deposit to the right account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Add the number the user has put using the AccountService created and the method deposit.
        accountService.deposit(account, amount);
        // direct to the dashboard but with the updated sum
        return "redirect:/dashboard";
    }

    /**
     * Process withdrawals, handles overdraft errors.
     *
     * @param userDetails authenticated user
     * @param amount      withdrawal amount
     * @param model       model to store error (if any)
     * @return redirect to dashboard regardless of success/failure
     */
    @PostMapping("/withdraw")
    // When the user uses the withdrawal form subtract the money from the account
    // If there is not enough money throw an error
    public String withdraw(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam BigDecimal amount,
                           Model model) {
        // Use the account model to pass the information to the account service and find the user
        // details from the username of the user
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // use the method try and catch
        try {
            // From the account service service use the withdraw method and subtract the amount the user wants
            accountService.withdraw(account, amount);
         // throw a runtime error if there is a failure on the withdrawal
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        //redirect to the appropriate dashboard
        return "redirect:/dashboard";
    }

    /**
     * View transaction history
     *
     * @param userDetails current user
     * @param model       Spring model
     * @return transaction.html view
     */
    @GetMapping("/transactions")
    public String viewTransactions(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Get all the transactions for the particular account
        model.addAttribute("transactions", accountService.getTransactionHistory(account));
        return "transaction"; // must match the transaction.html file name
    }

    /**
     * Transfer funds to another user by username.
     *
     * @param userDetails        authenticated user (sender)
     * @param recipient          recipient's username
     * @param amount             transfer amount
     * @param redirectAttributes flash attributes to show result
     * @return redirect to dashboard
     */
    @PostMapping("/transfer")
    // the authenticated principal is used to access an authenticated user directly in the controller methdo
    public String transfer(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam String recipient,
                           @RequestParam BigDecimal amount,
                           RedirectAttributes redirectAttributes) {
        try {
            accountService.transfer(userDetails.getUsername(), recipient, amount);
            redirectAttributes.addFlashAttribute("lastTransferRecipient", recipient);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard";
    }
}
