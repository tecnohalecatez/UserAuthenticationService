package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.dto.UserDTO;
import co.com.tecnohalecatez.api.dto.UserDataDTO;
import co.com.tecnohalecatez.model.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDate;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @InjectMocks
    private Handler handler;

    private User testUser;
    private UserDTO testUserDTO;
    private UserDataDTO testUserDataDTO;

    @BeforeEach
    void setUp() {
        testUser = buildTestUser(BigInteger.ONE, "john.doe@example.com");
        testUserDTO = buildTestUserDTO(BigInteger.ONE, "john.doe@example.com");
        testUserDataDTO = buildTestUserDataDTO("john.doe@example.com");
    }

    private static User buildTestUser(BigInteger id, String email) {
        return User.builder()
                .id(id)
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phone("555-1234")
                .email(email)
                .baseSalary(50000.0)
                .build();
    }

    private static UserDTO buildTestUserDTO(BigInteger id, String email) {
        return new UserDTO(
                id,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "555-1234",
                email,
                50000.0
        );
    }

    private static UserDataDTO buildTestUserDataDTO(String email) {
        return new UserDataDTO(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "555-1234",
                email,
                50000.0
        );
    }

    @Test
    void listenSaveUser_ReturnsCreated() {
        Mockito.when(handler.listenSaveUser(Mockito.any()))
                .thenReturn(Mono.just(org.springframework.web.reactive.function.server.ServerResponse.created(null).build().block()));

        webTestClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUserDataDTO)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void listenGetUserById_ReturnsUser() {
        Mockito.when(handler.listenGetUserById(Mockito.any()))
                .thenReturn(Mono.just(org.springframework.web.reactive.function.server.ServerResponse.ok().build().block()));

        webTestClient.get()
                .uri("/api/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void listenGetUserById_ReturnsNotFound() {
        Mockito.when(handler.listenGetUserById(Mockito.any()))
                .thenReturn(Mono.just(org.springframework.web.reactive.function.server.ServerResponse.notFound().build().block()));

        webTestClient.get()
                .uri("/api/users/999")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void listenDeleteUserById_ReturnsNoContent() {
        Mockito.when(handler.listenDeleteUserById(Mockito.any()))
                .thenReturn(Mono.just(org.springframework.web.reactive.function.server.ServerResponse.noContent().build().block()));

        webTestClient.delete()
                .uri("/api/users/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void listenGetAllUsers_ReturnsAllUsers() {
        Mockito.when(handler.listenGetAllUsers(Mockito.any()))
                .thenReturn(Mono.just(org.springframework.web.reactive.function.server.ServerResponse.ok().build().block()));

        webTestClient.get()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    private void assertGetEndpoint(String uri) {
        webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> Assertions.assertThat(response).isEmpty());
    }

    @Test
    void listenGETUseCase_ReturnsOkAndEmptyBody() {
        assertGetEndpoint("/api/usecase/path");
    }

    @Test
    void listenGETOtherUseCase_ReturnsOkAndEmptyBody() {
        assertGetEndpoint("/api/otherusercase/path");
    }

    @Test
    void listenPOSTUseCase_ReturnsOkAndEmptyBody() {
        webTestClient.post()
                .uri("/api/usecase/otherpath")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> Assertions.assertThat(response).isEmpty());
    }
}
