package org.example.config;

// Import the necessary libraries and Services to build the Security Configuration
import org.example.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for the Besparas Bank application.
 *
 * With this class the developer sets an HTTP Security, authentication providers, password encoding mechanisms
 * login and logout behaviours
 *
 * This configuration is crucial in ensuring secure access to customer data
 * preventing unauthorized behaviour operations in a financial domain
 *
 * @author Rafael Besparas
 */

// This line tell spring boot framework that this class will contain configuration settings
    // Like a blueprint for security
@Configuration
public class SecurityConfig {

    /**
     * Provides the user details service for authentication.
     *
     * @return a UserDetailsService implementation using AccountService
     */
    // This method returns a bean an object managed by Spring. It can be used anywhere at the application
    @Bean
    // This method also makes the Spring boot to use my custom Account Service to fetch user details such as username, password
    public UserDetailsService userDetailsService() {
        return new AccountService();
    }

    /**
     * BCrypt password encoder used for hashing user passwords.
     * BCrypt is recommended for secure storage due to built-in salting.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    //This creates a password encoder that securely hashes user passwords using B
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider to use DAO-based authentication
     * backed by the AccountService and BCrypt password encoding.
     *
     * @param accountService the custom user details service
     * @return DaoAuthenticationProvider wired with service and encoder
     */
    @Bean
    // This method does the authentication of the user
    public DaoAuthenticationProvider authProvider(AccountService accountService) {
        // This is the main component that checks the user credentials (Username and Password) during login
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Tells a provider to use the AccountService to find user details from the database
        authProvider.setUserDetailsService(accountService);
        // Tells the provider how to check the password by encoding it and also comparing it with the BCrypt data
        authProvider.setPasswordEncoder(passwordEncoder());
        // Return the setup from above and let the spring boot use it.
        return authProvider;
    }

    /**
     * Configures the security filter chain for HTTP security.
     * <ul>
     *   <li>Disables CSRF for simplicity (ensure it's enabled in production with tokens)</li>
     *   <li>Permits unauthenticated access to registration and login pages</li>
     *   <li>Secures all other routes</li>
     *   <li>Configures custom login and logout handling</li>
     * </ul>
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception on configuration failure
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disables CRSF protection which prevents certain attacks.
                .csrf(csrf -> csrf.disable())
                // Enables the non authenticated users to access the pages register and login
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**").permitAll()
                        // All other requests require the user to be logged in
                        .anyRequest().authenticated()
                )
                // Specifies the custom Login page URL
                .formLogin(login -> login
                        .loginPage("/login")
                        // After a successfull login, redirect the user to the dashboard
                        .defaultSuccessUrl("/dashboard", true)
                        // Allow everyone to access the login page.
                        .permitAll()
                )
                // After logout redirect the user login page with a message
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        // Allow everyone to access the logout page
                        .permitAll()
                );
        // Finalizes and returns the configurated security filter chain
        return http.build();
    }

    /**
     * Provides the authentication manager using Spring Boot's auto-configuration.
     *
     * @param config the authentication configuration context
     * @return the configured AuthenticationManager
     * @throws Exception on lookup failure
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // This gives the main Authentication manager knows how to handle login authentication
        return config.getAuthenticationManager();
    }
}
