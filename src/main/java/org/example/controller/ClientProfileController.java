package org.example.controller;

import org.example.model.AccountModel;
import org.example.model.ClientProfile;
import org.example.service.AccountService;
import org.example.service.ClientProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// This class manages the user profile, it allows the user to view and update their personal details
// The URL path is : /profile
@Controller // Mark the class as a controller that will show data to the user
@RequestMapping("/profile")  // Base URL path for all endpoints in the controller
public class ClientProfileController {

    // Add the account service to have access to account related business logic
    @Autowired
    private AccountService accountService;

    // Add the clientprofile service to have access to profile related services
    @Autowired
    private ClientProfileService clientProfileService;

    // Handles the get to show the users profile, If no profile exists an empty one is shown
    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Redirect to login if user is not logged in
        if (userDetails == null) {
            return "redirect:/login"; // redirect to the login page
        }

        // Get the account of the logged-in user
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        if (account == null) {
            return "redirect:/login"; // if there is not an account then redirect the user to the login page
        }

        // Load the user's profile
        ClientProfile profile = clientProfileService.getByAccount(account);

        // If no profile exists yet, show an empty one
        if (profile == null) {
            profile = new ClientProfile(); // show an empty form of the profile
        }

        // Send the profile data to the view using the model as a means of transportation of data
        model.addAttribute("profile", profile);
        return "profile-view"; // Display the profile view page
    }


    // Handle the edit request to edit the profile and show the user the edit form
    @GetMapping("/edit")
    public String editProfileForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Get the logged-in user's account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Check the account of the logged in user
        ClientProfile profile = clientProfileService.getByAccount(account);

        // Create a new profile if none exists
        if (profile == null) {
            profile = new ClientProfile(); // create a new profile and associate it with the account
            profile.setAccount(account); // set the account of the user
        }

        // Send the profile data to the edit form
        model.addAttribute("profile", profile);
        return "profile-edit"; // redirect to the edit profile
    }


    // Handle the edit function here I update the profile data after editing
    // Use the postmapping to get data from the user in order to Submit the Profile Changes
    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute("profile") ClientProfile profile,
                                @AuthenticationPrincipal UserDetails userDetails) {
        // Ensure the profile is linked to the logged-in user's account
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        profile.setAccount(account); // Set the association between the profile and the account

        // Save the updated profile data
        clientProfileService.saveProfile(profile);

        return "redirect:/profile"; // Redirect back to the profile view
    }
}
