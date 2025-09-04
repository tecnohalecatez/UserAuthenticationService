package co.com.tecnohalecatez.api.exception;

import co.com.tecnohalecatez.api.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ErrorResponseDTO errorResponse;
        int statusCode;
        String error;
        switch (ex) {
            case UserDataException userDataException -> {
                statusCode = HttpStatus.BAD_REQUEST.value();
                error = HttpStatus.BAD_REQUEST.getReasonPhrase();
            }
            case InvalidBearerTokenException invalidBearerTokenException -> {
                statusCode = HttpStatus.UNAUTHORIZED.value();
                error = HttpStatus.UNAUTHORIZED.getReasonPhrase();
            }
            case UserNotFoundException userNotFoundException -> {
                statusCode = HttpStatus.NOT_FOUND.value();
                error = HttpStatus.NOT_FOUND.getReasonPhrase();
            }
            default -> {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
                error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            }
        }
        errorResponse = new ErrorResponseDTO(
                Instant.now().toString(),
                statusCode,
                error,
                ex.getMessage()
        );
        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(errorResponse.status()));
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap(new ObjectMapper().writeValueAsBytes(errorResponse));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
