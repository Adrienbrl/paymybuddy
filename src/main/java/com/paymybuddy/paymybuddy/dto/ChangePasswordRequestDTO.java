package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Requête de changement de mot de passe.
 *
 * @param oldPassword ancien mot de passe.
 * @param newPassword nouveau mot de passe.
 */
public record ChangePasswordRequestDTO(
        @NotBlank @Size(min = 8, max = 100) String oldPassword,
        @NotBlank @Size(min = 8, max = 100) String newPassword
) { }

