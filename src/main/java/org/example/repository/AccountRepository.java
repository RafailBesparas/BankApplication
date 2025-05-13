package org.example.repository;

import org.example.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing {AccountModel} entities.
 * <p>
 * Extends Spring Data JPA's {JpaRepository} to provide standard CRUD operations,
 * along with custom account-specific queries.
 * <p>
 * <b>Security & Compliance:</b> Queries here operate on sensitive financial user data.
 * Ensure all service-level access to this repository is governed by authentication
 * and authorization layers (see {SecurityConfig}).
 *
 * <b>Performance:</b> Indexing the {username} column in the database is recommended
 * for high-throughput authentication and lookup scenarios.
 *
 * <b>Usage:</b> Inject this interface into services such as {AccountService} to perform
 * persistence and retrieval operations on user accounts.
 *
 * @author Rafael Besparas
 */

public interface AccountRepository extends JpaRepository<AccountModel, Long> {

    /**
     * Retrieves an account by its unique username.
     * <p>
     * This method is primarily used for login, account management, and user identity resolution.
     *
     * @param username the unique username of the account holder
     * @return {AccountModel} or {null} if not found
     */
    AccountModel findByUsername(String username);
}
