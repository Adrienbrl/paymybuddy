package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Requête d'ajout d'une relation utilisateur.
 *
 * @param otherUserId identifiant de l'utilisateur à ajouter.
 */
public record AddConnectionRequestDTO(
        @NotNull Integer otherUserId
) { }

