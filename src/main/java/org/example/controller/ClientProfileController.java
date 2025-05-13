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

/**
 * Controller for managing user profile data (ClientProfile).
 * <p>
 * This controller supports profile viewing and editing functionalities.
 * All operations are scoped to the currently authenticated user and protected
 * under Spring Security policies defined in {org.example.config.SecurityConfig}.
 *
 * <b>GDPR Note:</b> This component handles sensitive client PII, including email, address,
 * phone, and financial details. All access and updates must be logged and restricted.
 *
 * <b>Usage:</b> Mapped under the route { /profile}, used by client dashboard and settings.
 *
 * @author Rafael
 */

// Tells Spring boot that this class is a controller and will handle the HTTP requests
@Controller
// Tells Spring boot to handle requests under "/profile"  web route
@RequestMapping("/profile")
public class ClientProfileController {

    @Autowired
    // Inject the dependency account service.
    private AccountService accountService;

    @Autowired
    // Inject the dependencies for the Client Profile Service.
    private ClientProfileService clientProfileService;

    /**
     * GET handler for profile view.
     *
     * If no profile is found, a blank profile is created and populated in the view layer.
     *
     * @param userDetails Spring Security-authenticated user
     * @param model       Spring MVC model to bind attributes
     * @return profile-view.html
     */
    @GetMapping
    //When somebody visits the /profile route this method will run.
    // It uses the Spring security to get the logged in User
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // If user is not logged in then redirect him to the login page
        if (userDetails == null) {
            return "redirect:/login";
        }

        // Check the account of the user
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // If the account details are non existent then redirect the user to the login page
        if (account == null) {
            return "redirect:/login";
        }

        //Load the user profile using the ClientProfileService and show the profile view
        ClientProfile profile = clientProfileService.getByAccount(account);
        if (profile == null) {
            profile = new ClientProfile(); // create a blank profile if the user has not profile details or a profile
        }

        // in the model add the profile of the user
        model.addAttribute("profile", profile);
        // return the profile view
        return "profile-view";
    }

    /**
     * GET handler to render profile edit form.
     *
     * @param userDetails authenticated user
     * @param model       binds client profile to form
     * @return profile-edit.html
     */
    @GetMapping("/edit")
    // Redirect the user to the profile edit and show him the edit form
    public String editProfileForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Use the account service to get the information about the account of the user
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        // Use the ClientProfileService
        ClientProfile profile = clientProfileService.getByAccount(account);

        // If there is no Profile then create one and link the account
        if (profile == null) {
            profile = new ClientProfile();
            profile.setAccount(account); // link account
        }

        model.addAttribute("profile", profile);
        return "profile-edit"; // throw the profile edit form
    }

    /**
     * POST handler to update the profile.
     * <p>
     * Data submitted via form binding is persisted to the backend using {@link ClientProfileService}.
     *
     * @param profile     populated profile object from the form
     * @param userDetails current authenticated user
     * @return redirect to profile view page
     */
    @PostMapping("/edit")
    // When the user submits the edit form, this method saves the changes.
    public String updateProfile(@ModelAttribute("profile") ClientProfile profile,
                                @AuthenticationPrincipal UserDetails userDetails) {
        AccountModel account = accountService.getByUsername(userDetails.getUsername());
        profile.setAccount(account); // make a check and make sure the profile is tied to the correct user.
        clientProfileService.saveProfile(profile); // use  the clientprofileservice and the method to save the profile
        return "redirect:/profile"; // redirect the user to the profile.
    }
}
