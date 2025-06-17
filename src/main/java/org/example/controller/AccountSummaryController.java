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

// Controller that Handles the Account Summary Page
// Shows the account details and recent transactions for a logged-in user
@Controller // Use the controller to tell the Spring boot that this class will show content to the user
@RequestMapping("/account-summary")  // All URLs starting with /account-summary go to this controller
public class AccountSummaryController {

    // Spring will automatically provide an instance of AccountService
    @Autowired
    private AccountService accountService;

    /**
     * This method handles GET requests to /account-summary.
     * It shows the account summary for the currently logged-in user.
     */
    // This method handles the get requests from the account-summary url
    @GetMapping // Use get mapping to get information from the database and show it to the user
    public String showAccountSummary(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // If the user is not logged in, redirect them to the login page
        if (userDetails == null) {
            return "redirect:/login";
        }

        // Get the account data using the logged-in user's username
        AccountModel account = accountService.getByUsername(userDetails.getUsername());

        // If the account doesn't exist, redirect to login again
        if (account == null) {
            return "redirect:/login";
        }

        // Get the user's recent transactions
        List<Transaction> recentTransactions = accountService.getTransactionHistory(account);

        // Add account data and transactions to the model for use in the HTML page
        model.addAttribute("accountHolder", account.getUsername());
        model.addAttribute("accountNumber", account.getId());
        model.addAttribute("accountType", "Standard"); // You can change this later if needed
        model.addAttribute("balance", account.getBalance());
        model.addAttribute("recentTransactions", recentTransactions);

        // Show the account-summary.html page
        return "account-summary";
    }
}
