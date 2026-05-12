package com.library.dto;

import jakarta.validation.constraints.NotBlank;

public record BookDTO(

        @NotBlank(message = "Title must not be blank")
        String title,

        @NotBlank(message = "Author must not be blank")
        String author
) {}
