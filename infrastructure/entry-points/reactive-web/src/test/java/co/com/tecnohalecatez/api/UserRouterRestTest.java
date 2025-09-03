package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.config.UserPath;
import co.com.tecnohalecatez.api.dto.UserDTO;
import co.com.tecnohalecatez.api.dto.UserDataDTO;
import co.com.tecnohalecatez.api.exception.GlobalExceptionHandler;
import co.com.tecnohalecatez.api.mapper.UserDTOMapper;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import org.springframework.validation.Validator;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class, GlobalExceptionHandler.class})
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
    private UserDTOMapper userDTOMapper;

    @MockitoBean
    private Validator validator;

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
            .id(BigInteger.TWO)
            .name("Johns")
            .surname("Does")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("123 Main Sts")
            .phone("555-12345")
            .email("john.does@example.com")
            .baseSalary(150000.0)
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
        assertEquals(users + "/{id}", userPath.getUsersById());
    }

    @Test
    void listenSaveUserReturnsCreated() {
        UserDTO expectedUserDTO = new UserDTO(BigInteger.ONE, "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234", "john.doe@example.com", 50000.0);
        doNothing().when(validator).validate(any(), any());
        when(userUseCase.existsByEmail("john.doe@example.com")).thenReturn(Mono.just(false));
        when(userDTOMapper.toModel(testUserDataDTO)).thenReturn(testUserOne);
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(testUserOne));
        when(userDTOMapper.toResponse(testUserOne)).thenReturn(expectedUserDTO);
        webTestClient.post()
                .uri(users)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUserDataDTO)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void listenGetUserByIdReturnsUser() {
        UserDTO expectedUserDTO = new UserDTO(BigInteger.ONE, "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234", "john.doe@example.com", 50000.0);
        when(userUseCase.getUserById(BigInteger.ONE)).thenReturn(Mono.just(testUserOne));
        when(userDTOMapper.toResponse(testUserOne)).thenReturn(expectedUserDTO);
        webTestClient.get()
                .uri(users + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(userDTO ->
                        Assertions.assertThat(userDTO.id()).isEqualTo(BigInteger.ONE));
    }

    @Test
    void listenGetUserByIdReturnsNotFound() {
        when(userUseCase.getUserById(BigInteger.valueOf(999))).thenReturn(Mono.empty());
        webTestClient.get()
                .uri(users + "/999")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void listenUpdateUserReturnsUpdatedUser() {
        UserDTO expectedUserDTO = new UserDTO(BigInteger.ONE, "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234", "john.doe@example.com", 50000.0);
        doNothing().when(validator).validate(any(), any());
        when(userUseCase.getUserById(BigInteger.ONE)).thenReturn(Mono.just(testUserOne));
        when(userDTOMapper.toModel(testUserDataDTO)).thenReturn(testUserOne);
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(testUserOne));
        when(userDTOMapper.toResponse(testUserOne)).thenReturn(expectedUserDTO);
        webTestClient.put()
                .uri(users + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUserDataDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(userDTO ->
                        Assertions.assertThat(userDTO.id()).isEqualTo(BigInteger.ONE));
    }

    @Test
    void listenDeleteUserByIdReturnsNoContent() {
        when(userUseCase.getUserById(BigInteger.ONE)).thenReturn(Mono.just(testUserOne));
        when(userUseCase.deleteUserById(BigInteger.ONE)).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri(users + "/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void listenGetAllUsersReturnsAllUsers() {
        UserDTO userDTO1 = new UserDTO(BigInteger.ONE, "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234", "john.doe@example.com", 50000.0);
        UserDTO userDTO2 = new UserDTO(BigInteger.TWO, "Johns", "Does", LocalDate.of(1990, 1, 1), "123 Main Sts", "555-12345", "john.does@example.com", 150000.0);
        when(userUseCase.findAllUsers()).thenReturn(Flux.just(testUserOne, testUserTwo));
        when(userDTOMapper.toResponse(testUserOne)).thenReturn(userDTO1);
        when(userDTOMapper.toResponse(testUserTwo)).thenReturn(userDTO2);
        webTestClient.get()
                .uri(users)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .hasSize(2)
                .value(userList -> {
                    Assertions.assertThat(userList).isNotEmpty();
                    Assertions.assertThat(userList.getFirst().id()).isEqualTo(BigInteger.ONE);
                })
        ;
    }

}

