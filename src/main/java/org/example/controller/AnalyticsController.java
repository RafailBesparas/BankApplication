package org.example.controller;

import org.example.model.AccountModel;
import org.example.service.AccountService;
import org.example.service.TransactionAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

// Controller that handles the requests for Analytics Dashboards
// The target in the next steps would be to show the monthly spending by each category for the logged in user
// Path: /analytics
@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    // Injecting the AccountService to get user account information
    @Autowired
    private AccountService accountService;

    // Injecting the Analytics Service to calculate and provide analytics data
    @Autowired
    private TransactionAnalyticsService analyticsService;

    // Handle the get requests to /analytics
    // Gets all account details of the logged-in user and add them to analytics data view
    @GetMapping
    public String showDashboard(Model model, Principal principal) {
        // Get the account of the current user using their username
        AccountModel user = accountService.getByUsername(principal.getName());

        // Add total spending per month to the model (used in charts or tables)
        model.addAttribute("monthlySpending", analyticsService.getMonthlySpending(user));

        // Add spending breakdown by category (future feature, possibly ML-powered)
        model.addAttribute("categorySpending", analyticsService.getSpendingByCategory(user));

        // Return the dashboard view located in templates/analytics/dashboard.html
        return "analytics/dashboard";
    }
}
