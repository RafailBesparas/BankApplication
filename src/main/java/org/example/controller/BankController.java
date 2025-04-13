package org.example.controller;

import org.example.model.AccountModel;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class BankController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new AccountModel());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AccountModel account) {
        accountService.register(account);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal AccountModel account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("transactions", accountService.getTransactionHistory(account));
        return "dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(@AuthenticationPrincipal AccountModel account,
                          @RequestParam BigDecimal amount,
                          Model model) {
        accountService.deposit(account, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@AuthenticationPrincipal AccountModel account,
                           @RequestParam BigDecimal amount,
                           Model model) {
        try {
            accountService.withdraw(account, amount);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
