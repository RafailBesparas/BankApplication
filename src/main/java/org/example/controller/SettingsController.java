package org.example.controller;

import org.example.model.AccountModel;
import org.example.model.UserSettings;
import org.example.service.AccountService;
import org.example.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


// This controller handles the user settings, it shows the page and saves updates made by the user
@Controller // this controller shows views
@RequestMapping("/settings") // endpoint : /settings
public class SettingsController {

    // Inject the AccountService to handle the logic related to the users account
    @Autowired
    private AccountService accountService;

    // Inject the UserSettings to handles the logic related to the user settings
    @Autowired
    private UserSettingsService settingsService;


    // Handles the get mapping and shows the settings page for the current user
    @GetMapping
    public String showSettings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Get the logged-in user's account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());

        // Try to get existing settings for this account
        UserSettings settings = settingsService.getByAccount(account);

        // If no settings exist, create default ones
        if (settings == null) {
            settings = settingsService.createDefault(account);
        }

        // Send the settings to the HTML page, add the settings for the user to the view model
        model.addAttribute("settings", settings);

        // Show the settings.html view
        return "settings";
    }

    // Handles the data and the updated settings that the user submitted
    @PostMapping
    public String updateSettings(@AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute UserSettings form) {
        // Get the user's account again
        AccountModel account = accountService.getByUsername(userDetails.getUsername());

        // Make sure the settings are linked to this account
        form.setAccount(account);

        // Save the settings to the database
        settingsService.saveSettings(form);

        // Go back to the settings page with a success query parameter
        return "redirect:/settings?success";
    }
}
