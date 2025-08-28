package co.com.tecnohalecatez.api.dto;

import java.util.List;

public record ErrorReponseDTO(
        String timestamp,
        int status,
        String error,
        String message) {
}
