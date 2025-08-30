package co.com.tecnohalecatez.api.config;

import co.com.tecnohalecatez.api.UserHandler;
import co.com.tecnohalecatez.api.UserRouterRest;
import co.com.tecnohalecatez.api.mapper.UserDTOMapper;
import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class, UserPath.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserPath userPath;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    private final User testUserOne = User.builder()
            .id(BigInteger.ONE)
            .name("John")
            .surname("Doe")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("123 Main St")
            .phone("555-1234")
            .email("john.doe@example.com")
            .baseSalary(50000.0)
            .build();

    private final User testUserTwo = User.builder()
            .id(BigInteger.ONE)
            .name("John")
            .surname("Doe")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("123 Main St")
            .phone("555-1234")
            .email("john.doe@example.com")
            .baseSalary(50000.0)
            .build();

    @BeforeEach
    void setUp() {
        when(userUseCase.findAllUsers()).thenReturn(Flux.just(testUserOne, testUserTwo));
    }

    @Test
    void shouldLoadUserPathProperties() {
        assertEquals("/api/v1/users", userPath.getUsers());
        assertEquals("/api/v1/users/{id}", userPath.getUsersById());
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/v1/u")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}