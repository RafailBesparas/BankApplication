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

/**
 * Controller responsible for handling requests related to transaction analytics.
 * This includes showing the user's monthly spending and category-based expenditure.
 *
 * URL Path: /analytics
 *
 * Access is secured and tied to the authenticated user's principal.
 *
 * Dependencies:
 * - AccountService: to retrieve account details based on the logged-in user.
 * - TransactionAnalyticsService: to compute and return analytical data.
 */

// Controller bean tells the Spring Boot that this class with be the Contoller in the MVC Architecture
    // So it will handle the HTTP requests from my website
@Controller
// When the user visit this page run this code in this controller class
@RequestMapping("/analytics")
public class AnalyticsController {

    // Inject the depencency Autowired so that I will have the necessary model to work with
    // We need this to access account information
    @Autowired
    private AccountService accountService;

    // Import the dependencies needed for the Analytics Service for transactions
    // Get the transaction analytics data
    @Autowired
    private TransactionAnalyticsService analyticsService;

    /**
     * Handles GET requests to "/analytics".
     * Fetches the currently logged-in user's account and passes spending analytics to the view.
     *
     * @param model Spring's UI model used to pass attributes to the Thymeleaf (or JSP) view
     * @param principal Authenticated user's principal, used to identify the currently logged-in user
     * @return View name of the analytics dashboard
     */
    @GetMapping
    public String showDashboard(Model model, Principal principal) {

        // Get the account details of the current user and their username
        AccountModel user = accountService.getByUsername(principal.getName());

        // add the monthly spending data to the model
        model.addAttribute("monthlySpending", analyticsService.getMonthlySpending(user));

        // Add the category based spending to the model (future feature that will be implemented with machine learning)
        model.addAttribute("categorySpending", analyticsService.getSpendingByCategory(user));

        // Present to the user the view analytics dashboard
        return "analytics/dashboard";
    }
}