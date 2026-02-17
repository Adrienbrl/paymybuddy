package com.paymybuddy.paymybuddy.dto;

import java.math.BigDecimal;

/**
 * Réponse API d'un transfert affichable côté UI.
 *
 * @param id identifiant du transfert.
 * @param relationName nom de la contrepartie (destinataire ou expéditeur selon le contexte).
 * @param description libellé du transfert.
 * @param amount montant du transfert.
 */
public record TransferResponseDTO(
        Integer id,
        String relationName,
        String description,
        BigDecimal amount
) { }

