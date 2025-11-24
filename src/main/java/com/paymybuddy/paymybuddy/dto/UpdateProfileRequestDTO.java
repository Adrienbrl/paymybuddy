package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequestDTO(
        @Size(max = 100) String username,
        @Email @Size(max = 255) String email
) { }
