package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository JPA des utilisateurs.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Recherche par email unique.
     */
    Optional<User> findByEmail(String email);

    /**
     * Recherche par username unique.
     */
    Optional<User> findByUsername(String username);

    /**
     * Vérifie l'existence d'un email.
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie l'existence d'un username.
     */
    boolean existsByUsername(String username);
}
