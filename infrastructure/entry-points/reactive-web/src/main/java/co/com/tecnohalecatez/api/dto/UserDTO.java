package co.com.tecnohalecatez.api.dto;

import java.math.BigInteger;
import java.time.LocalDate;

public record UserDTO(
        BigInteger id,
        String name,
        String surname,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        Double baseSalary) {
}
