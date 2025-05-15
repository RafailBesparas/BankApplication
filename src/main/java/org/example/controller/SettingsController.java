package org.example.controller;

import org.springframework.ui.Model;
import org.example.model.AccountModel;
import org.example.model.UserSettings;
import org.example.service.AccountService;
import org.example.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserSettingsService settingsService;

    @GetMapping
    public String showSettings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        UserSettings settings = settingsService.getByAccount(account);
        if (settings == null) settings = settingsService.createDefault(account);

        model.addAttribute("settings", settings);
        return "settings";
    }

    @PostMapping
    public String updateSettings(@AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute UserSettings form) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        form.setAccount(account); // maintain link
        settingsService.saveSettings(form);
        return "redirect:/settings?success";
    }
}