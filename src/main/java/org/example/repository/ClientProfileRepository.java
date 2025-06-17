package org.example.repository;

import org.example.model.ClientProfile;
import org.example.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

// Interface to interact with the database
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    // this method retrieves the profile linked to a given account
    ClientProfile findByAccount(AccountModel account);
}
