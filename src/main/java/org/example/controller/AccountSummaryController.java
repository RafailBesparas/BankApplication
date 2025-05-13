package org.example.controller;

import org.example.model.AccountModel;
import org.example.model.Transaction;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller responsible for rendering the account summary page.
 * <p>
 * Accessible only to authenticated users, this controller loads the current user's
 * account data and recent transactions to be displayed on the summary dashboard.
 * <p>
 * <b>Security:</b> All requests to this controller are protected by Spring Security.
 * CSRF, authentication, and role-based access are enforced globally in {@code SecurityConfig}.
 *
 * <p>
 * <b>Compliance:</b> Designed to support banking-grade user data handling. Avoids exposing
 * sensitive information such as passwords or internal IDs in the frontend.
 *
 * @author Rafael Besparas
 */

// This marks the class here from the Spring MVC design Patter,  as controller which handles the http requests and reutrns views
@Controller
// Maps all requests that starts with /account-summary to this controller
@RequestMapping("/account-summary")
public class AccountSummaryController {

    // Automatic Dependency Injection
    @Autowired
    // Injects the AccountService so we can use it to get account and transactions data from the Service layer
    private AccountService accountService;


    /**
     * Handles GET requests for the account summary page.
     * <p>
     * The method retrieves account information and recent transactions of the currently
     * authenticated user and binds them to the model for Thymeleaf rendering.
     *
     * @param userDetails authenticated user from Spring Security context
     * @param model       the model to bind view data
     * @return the name of the Thymeleaf view template to render
     */
    // Handles the HTTP Requests
    @GetMapping
    // This method will run when a logged in user visits the account summary controller
    // It only receives the logged in user details and a Model object to send data to the view
    public String showAccountSummary(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // If the user is not logged in then redirect them to the login page
        if (userDetails == null) {
            return "redirect:/login";
        }

        // Get the account info based on the logged in user information
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // If the user account is not found redirect them to the login again.
        if (account == null) {
            return "redirect:/login";
        }

        // Gets a list of recent transactions for that specific account that the user has.
        List<Transaction> recentTransactions = accountService.getTransactionHistory(account);

        // Add a lot of account details and the transaction to the model
        // Add the accountHolder, The account Number, the accountType and the balance.
        model.addAttribute("accountHolder", account.getUsername());
        model.addAttribute("accountNumber", account.getId());
        model.addAttribute("accountType", "Standard");
        model.addAttribute("balance", account.getBalance());
        model.addAttribute("recentTransactions", recentTransactions);

        // Tells the Spring to show the account-summary.html page
        return "account-summary";
    }
}
