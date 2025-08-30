package co.com.tecnohalecatez.api.dto;

public record ErrorReponseDTO(
        String timestamp,
        int status,
        String error,
        String message) {
}
