package com.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberDTO(

        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be a valid address")
        String email
) {}

