package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.config.LoginPath;
import co.com.tecnohalecatez.api.dto.LoginDTO;
import co.com.tecnohalecatez.api.dto.LoginDataDTO;
import co.com.tecnohalecatez.api.exception.GlobalExceptionHandler;
import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {LoginRouterRest.class, LoginHandler.class, GlobalExceptionHandler.class})
@EnableConfigurationProperties(LoginPath.class)
@WebFluxTest
class LoginRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private LoginPath loginPath;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private Validator validator;

    private final User testUser = User.builder()
            .id(BigInteger.ONE)
            .name("John")
            .surname("Doe")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("123 Main St")
            .phone("555-1234")
            .email("john.doe@example.com")
            .password("password123")
            .roleId(1)
            .baseSalary(50000.0)
            .build();

    private final LoginDataDTO testValidLoginData = new LoginDataDTO(
            "john.doe@example.com",
            "password123"
    );

    private final LoginDataDTO testInvalidLoginData = new LoginDataDTO(
            "invalid-email",
            "password123"
    );

    private final String loginEndpoint = "/api/v1/login";

    @Test
    void shouldLoadLoginPathProperties() {
        assertEquals(loginEndpoint, loginPath.getLogin());
    }

    @Test
    void listenGetTokenReturnsTokenWhenValidCredentials() {
        doNothing().when(validator).validate(any(), any());
        when(userUseCase.existsByEmailAndPassword(testValidLoginData.email(), testValidLoginData.password()))
                .thenReturn(Mono.just(true));
        when(userUseCase.findByEmailAndPassword(testValidLoginData.email(), testValidLoginData.password()))
                .thenReturn(Mono.just(testUser));

        webTestClient.post()
                .uri(loginEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testValidLoginData)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginDTO.class)
                .value(loginDTO ->
                        Assertions.assertThat(loginDTO.token()).isNotNull());
    }

    @Test
    void listenGetTokenReturnsErrorWhenInvalidCredentials() {
        doNothing().when(validator).validate(any(), any());
        when(userUseCase.existsByEmailAndPassword(testInvalidLoginData.email(), testInvalidLoginData.password()))
                .thenReturn(Mono.just(false));

        webTestClient.post()
                .uri(loginEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testInvalidLoginData)
                .exchange()
                .expectStatus().isBadRequest();
    }

}
