package org.example.service;

import org.example.model.AccountModel;
import org.example.model.ClientProfile;
import org.example.repository.ClientProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for accessing and managing client profile information.
 * <p>
 * Acts as an abstraction over the {@link ClientProfileRepository} and provides
 * methods to retrieve and persist Personally Identifiable Information (PII)
 * related to banking users.
 *
 * <b>Compliance:</b> This service deals with sensitive identity fields such as
 * national ID, income, address, and contact info. Proper access controls and secure
 * usage patterns must be enforced in the controller layer.
 *
 * <b>Responsibilities:</b>
 * <ul>
 *     <li>Encapsulates access to profile storage</li>
 *     <li>Prepares profiles for form pre-population</li>
 *     <li>Supports updates and new client onboarding</li>
 * </ul>
 *
 * <b>Security Note:</b> Never expose this data directly to logs or clients
 * without redaction/masking.
 *
 * @author Rafael Besparas
 */

// Tells spring boot that this is a service component handling the business logic
@Service
public class ClientProfileService {

    // // Spring injects the repository that talks to the database for ClientProfile operations.
    @Autowired
    private ClientProfileRepository profileRepo;

    /**
     * Retrieves the client profile associated with a given account.
     * <p>
     * This method is typically used to populate profile forms or dashboards.
     *
     * @param account the associated {@link AccountModel}
     * @return the {@link ClientProfile} or {@code null} if none is found
     */
    // // Calls the repository to fetch a profile associated with the given account.
    public ClientProfile getByAccount(AccountModel account) {
        return profileRepo.findByAccount(account);
    }

    /**
     * Saves or updates the given client profile.
     * <p>
     * Can be used for both new registrations and profile modifications.
     *
     * @param profile the profile entity to persist
     */
    //Saves a new profile or updates an existing one in the database.
    public void saveProfile(ClientProfile profile) {
        profileRepo.save(profile);
    }
}
