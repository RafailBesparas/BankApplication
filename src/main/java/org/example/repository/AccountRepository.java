package org.example.repository;

import org.example.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {


    Optional<AccountModel> findByUsername(String username);
}
