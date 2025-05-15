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

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionAnalyticsService analyticsService;

    @GetMapping
    public String showDashboard(Model model, Principal principal) {
        AccountModel user = accountService.getByUsername(principal.getName());

        model.addAttribute("monthlySpending", analyticsService.getMonthlySpending(user));
        model.addAttribute("categorySpending", analyticsService.getSpendingByCategory(user));
        return "analytics/dashboard";
    }
}