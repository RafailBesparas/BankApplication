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

// Tells the spring boot to make this class a web controller
// @RequestMapping("/settings"): All URLs in this class will start with /settings.
@Controller
@RequestMapping("/settings")
public class SettingsController {

    // Get the current user’s account info.
    @Autowired
    private AccountService accountService;

    // Read or save the user's settings.
    @Autowired
    private UserSettingsService settingsService;

    // Show the setting page
    @GetMapping
    public String showSettings(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        // Get the current log in user data
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Gets the user's settings, or creates default ones
        UserSettings settings = settingsService.getByAccount(account);
        // Gets the user's settings, or creates default ones if they don’t exist
        if (settings == null) settings = settingsService.createDefault(account);

        // Add the setting to the model and let them be displayed using HTML
        model.addAttribute("settings", settings);
        return "settings"; // Loads the settings HTML
    }

    // Save and update setting from the user
    @PostMapping
    // Gets the current user (like before)
    // Takes the form input from the HTML (Spring automatically maps form fields into the UserSettings object)
    public String updateSettings(@AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute UserSettings form) {
        // Use the username to link the user with his user account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Links the form data to the user’s account
        form.setAccount(account); // maintain link
        // Saves the settings using your service:
        settingsService.saveSettings(form);
        // Redirects back using the success flag
        return "redirect:/settings?success";
    }
}