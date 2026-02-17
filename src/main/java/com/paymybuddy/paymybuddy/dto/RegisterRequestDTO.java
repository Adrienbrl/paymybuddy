package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Requête d'inscription.
 *
 * @param username pseudo de l'utilisateur.
 * @param email email de connexion.
 * @param password mot de passe en clair.
 */
public record RegisterRequestDTO(
        @NotBlank @Size(max = 100) String username,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8, max = 100) String password
) { }

