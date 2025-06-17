package org.example.service;

import org.example.model.AccountModel;
import org.example.model.ClientProfile;
import org.example.repository.ClientProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// This services handles the user profile data like name, address, email and income
@Service
public class ClientProfileService {

    // Inject the ClientProfileRepository in order to have access to crud methods from the database
    @Autowired
    private ClientProfileRepository profileRepo;

    // Find the profile for each account in order to show the user profile to the screen
    public ClientProfile getByAccount(AccountModel account) {
        return profileRepo.findByAccount(account);
    }

    // Save or update a user profile information
    public void saveProfile(ClientProfile profile) {
        profileRepo.save(profile);
    }

    // Create a default empty profile for new users that will be filled later
    public ClientProfile createDefault(AccountModel account) {
        ClientProfile profile = new ClientProfile();
        profile.setAccount(account);
        return profileRepo.save(profile);
    }
}
