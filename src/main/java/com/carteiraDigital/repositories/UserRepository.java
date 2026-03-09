package com.carteiraDigital.repositories;

import com.carteiraDigital.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findUserByDocument(String document);
    Optional<UserModel> findUserById(Long id);
}
