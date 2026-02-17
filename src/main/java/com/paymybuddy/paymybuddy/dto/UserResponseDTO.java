package com.paymybuddy.paymybuddy.dto;

/**
 * Représentation publique d'un utilisateur.
 *
 * @param id identifiant technique.
 * @param username pseudonyme.
 * @param email email.
 */
public record UserResponseDTO(
        Integer id,
        String username,
        String email
) { }

