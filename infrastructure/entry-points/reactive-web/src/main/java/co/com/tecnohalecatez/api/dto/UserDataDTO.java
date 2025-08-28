package co.com.tecnohalecatez.api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserDataDTO(
        @NotBlank(message = "Name must not be blank")
        String name,
        @NotBlank(message = "Surname must not be blank")
        String surname,
        LocalDate birthDate,
        String address,
        String phone,
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid")
        String email,
        @NotNull(message = "Base salary is required")
        @Positive(message = "Base salary must be positive")
        @DecimalMin(value = "0.0", inclusive = true, message = "Base salary must be at least 0")
        @DecimalMax(value = "15000000.0", inclusive = true, message = "Base salary must not exceed 15,000,000")
        Double baseSalary) {
}
