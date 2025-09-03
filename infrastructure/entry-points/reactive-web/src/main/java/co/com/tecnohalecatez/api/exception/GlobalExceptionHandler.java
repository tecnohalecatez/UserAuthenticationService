package co.com.tecnohalecatez.api.exception;

import co.com.tecnohalecatez.api.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
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
        int statusCode = 500;
        String error = "Internal Server";
        if (ex instanceof UserDataException) {
            statusCode = 400;
            error = "Bad Request";
        } else if (ex instanceof UserNotFoundException) {
            statusCode = 404;
            error = "Not Found";
        }
        errorResponse = new ErrorResponseDTO(
                Instant.now().toString(),
                statusCode,
                error,
                ex.getMessage()
        );
        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(statusCode));
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap(new ObjectMapper().writeValueAsBytes(errorResponse));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
