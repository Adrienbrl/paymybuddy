package com.paymybuddy.paymybuddy.dto;

import java.math.BigDecimal;

public record TransferResponseDTO(
        Integer id,
        String relationName,
        String description,
        BigDecimal amount
) { }

