package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Requête de mise à jour de profil.
 *
 * @param username nouveau pseudonyme (optionnel).
 * @param email nouvel email (optionnel).
 */
public record UpdateProfileRequestDTO(
        @Size(max = 100) String username,
        @Email @Size(max = 255) String email
) { }
