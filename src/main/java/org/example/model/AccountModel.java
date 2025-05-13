package org.example.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;


/**
 * Primary domain model representing a customer account in BesparasBank.
 * <p>
 * This entity integrates Spring Security's { UserDetails} for authentication
 * and serves as the persistence object for all user financial state and identity.
 *
 * <p><b>Design Note:</b> This model consolidates login credentials, balance, transactions,
 * and profile details. It is used in both security contexts and domain logic.
 *
 * <b>Compliance:</b> Sensitive information (e.g. username, password, balance) must not be exposed
 * directly to views or logs. Masking and encryption policies should be applied as needed.
 *
 * <b>Persistence:</b> JPA-managed. Uses IDENTITY strategy for primary key generation.
 *
 * @author Rafael Besparas
 */
@Entity
// Tells JPA Java Persistence API that this class maps to a database table.
public class AccountModel implements UserDetails {

    // Annotation in the Spring Data JPA is used to mark the primary Key of an entity
    // Generate its value when a row is inserted into the database.
    @Id
    // Tells the database to auto-increment the ID field
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // primary key for the account table
    private Long id;

    // Variables needed for the Account model such  the account's login username, hashed password, and current balance
    private String username;
    private String password;
    private BigDecimal balance;

    // Relationship on the database one account can have many transactions
    // mapped by means that the transactions entity has a field named account that owns the relationship
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    // Relationship on the database that means one client is allowed to have on account
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    // Mapped by refers to the account field in the ClientProfile.
    private ClientProfile clientProfile;

    @Transient // do not save this to the database
    // This holds the roles and permissions and is not saved in the database
    private Collection<? extends GrantedAuthority> authorities;

    // This is a default constructor for Frameworks like Spring and JPA
    public AccountModel() {}

    /**
     * Constructor with fields for login and transactional context.
     *
     * @param username    user identifier
     * @param password    hashed password
     * @param balance     current balance
     * @param transactions linked transaction list
     * @param authorities security roles/permissions
     */
    public AccountModel(String username, String password, BigDecimal balance, List<Transaction> transactions, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.transactions = transactions;
        this.authorities = authorities;
    }

    // Public Getters and Setter for the Attributes of the Account Model

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public ClientProfile getClientProfile() { return clientProfile; }

    /**
     * Sets the client profile and ensures bidirectional association.
     *
     * @param clientProfile the profile to associate with this account
     */

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
        if (clientProfile != null) {
            clientProfile.setAccount(this); // maintain bidirectional link
        }
    }

    // Ensure that the developer actually override a method from a parent class or interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    // Returns true if the user account is not expired
    // Returns false id the user account when the user account expired
    @Override
    public boolean isAccountNonExpired() { return true; }

    // Returns true if the user account is not locked
    // Return false if there are for example too many failed login attempts
    @Override
    public boolean isAccountNonLocked() { return true; }

    // Return true if the user password is still valid
    // Return false if the password needs to change periodically
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    // Returns true is the user is enabled active
    // Returns false to a disabled user
    @Override
    public boolean isEnabled() { return true; }
}
