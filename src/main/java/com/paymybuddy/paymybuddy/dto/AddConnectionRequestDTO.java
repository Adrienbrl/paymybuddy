package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;

public record AddConnectionRequestDTO(
        @NotNull Integer otherUserId
) { }

