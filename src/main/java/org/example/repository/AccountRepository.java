package org.example.repository;

import org.example.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    AccountModel findByUsername(String username);
}
