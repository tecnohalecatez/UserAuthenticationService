package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.config.UserPath;
import co.com.tecnohalecatez.api.dto.UserDataDTO;
import co.com.tecnohalecatez.api.mapper.UserDTOMapper;
import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@EnableConfigurationProperties(UserPath.class)
@WebFluxTest
class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserPath userPath;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserHandler userHandler;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    private final User testUser = User.builder()
            .id(BigInteger.ONE)
            .name("John")
            .surname("Doe")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("123 Main St")
            .phone("555-1234")
            .email("john.doe@example.com")
            .baseSalary(50000.0)
            .build();

    private final UserDataDTO testUserDataDTO = new UserDataDTO(
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            "555-1234",
            "john.doe@example.com",
            50000.0
    );

    private final String users = "/api/v1/users";

    @Test
    void shouldLoadUserPathProperties() {
        assertEquals(users, userPath.getUsers());
        assertEquals("/api/v1/users/{id}", userPath.getUsersById());
    }

    @Test
    void listenSaveUserReturnsCreated() {
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(testUser));
        Mockito.when(userHandler.listenSaveUser(Mockito.any()))
                .thenReturn(Mono.just(ServerResponse.created(null).build().block()));
        webTestClient.post()
                .uri(users)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUserDataDTO)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void listenGetUserByIdReturnsUser() {
        when(userUseCase.getUserById(BigInteger.ONE)).thenReturn(Mono.just(testUser));
        Mockito.when(userHandler.listenGetUserById(Mockito.any()))
                .thenReturn(Mono.just(ServerResponse.ok().build().block()));

        webTestClient.get()
                .uri("/api/v1/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void listenGetUserByIdReturnsNotFound() {
        Mockito.when(userHandler.listenGetUserById(Mockito.any()))
                .thenReturn(Mono.just(ServerResponse.notFound().build().block()));

        webTestClient.get()
                .uri("/api/v1/users/999")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void listenDeleteUserByIdReturnsNoContent() {
        when(userUseCase.deleteUserById(BigInteger.ONE)).thenReturn(Mono.empty());
        Mockito.when(userHandler.listenDeleteUserById(Mockito.any()))
                .thenReturn(Mono.just(ServerResponse.noContent().build().block()));
        webTestClient.delete()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void listenGetAllUsersReturnsAllUsers() {
        when(userUseCase.findAllUsers()).thenReturn(Flux.just(testUser));
        Mockito.when(userHandler.listenGetAllUsers(Mockito.any()))
                .thenReturn(Mono.just(ServerResponse.ok().build().block()));

        webTestClient.get()
                .uri(users)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

}

