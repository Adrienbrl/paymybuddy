package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Requête de connexion.
 *
 * @param email email utilisateur.
 * @param password mot de passe en clair.
 */
public record LoginRequestDTO(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8, max = 100) String password
) { }

