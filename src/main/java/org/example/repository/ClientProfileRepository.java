package org.example.repository;

import org.example.model.ClientProfile;
import org.example.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on {ClientProfile} entities.
 * <p>
 * Provides a linkage between account credentials and their associated profile details,
 * including sensitive PII such as address, email, and national ID.
 *
 * <b>Compliance:</b> This repository handles personal data and must be accessed only
 * by authenticated and authorized services. Logs, debugging tools, or metrics collection
 * must never expose this data.
 *
 * <b>Indexing Note:</b> Ensure the {account_id} field is indexed in the database
 * for optimal join and lookup performance.
 *
 * <b>Usage:</b> Typically used by services like {org.example.service.ClientProfileService}
 * to load or persist client profile updates.
 *
 * @author Rafael Besparas
 */

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    /**
     * Retrieves a profile linked to a given account.
     *
     * @param account the account entity associated with the profile
     * @return the {ClientProfile} object, or {null} if none exists
     */
    ClientProfile findByAccount(AccountModel account);
}
