package org.example.controller;

import org.example.model.AccountModel;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails,
                            Model model,
                            @ModelAttribute("lastTransferRecipient") String lastTransferRecipient) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        model.addAttribute("account", account);
        model.addAttribute("transactions", accountService.getTransactionHistory(account));
        if (lastTransferRecipient != null && !lastTransferRecipient.isBlank()) {
            model.addAttribute("lastTransferRecipient", lastTransferRecipient);
        }
        return "dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam BigDecimal amount) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        accountService.deposit(account, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam BigDecimal amount,
                           Model model) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        try {
            accountService.withdraw(account, amount);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/transactions")
    public String viewTransactions(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        model.addAttribute("transactions", accountService.getTransactionHistory(account));
        return "transaction"; // must match the transaction.html file name
    }

    @PostMapping("/transfer")
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
