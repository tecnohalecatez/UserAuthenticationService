package co.com.tecnohalecatez.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDataDTO(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password) {
}
