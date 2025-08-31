package co.com.tecnohalecatez.api.dto;

public record ErrorResponseDTO(
        String timestamp,
        int status,
        String error,
        String message) {
}
