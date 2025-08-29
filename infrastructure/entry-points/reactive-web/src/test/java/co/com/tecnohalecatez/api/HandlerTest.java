package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.dto.UserDTO;
import co.com.tecnohalecatez.api.dto.UserDataDTO;
import co.com.tecnohalecatez.api.mapper.UserDTOMapper;
import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private UserDTOMapper userDTOMapper;

    @InjectMocks
    private Handler handler;

    private User testUser;
    private UserDTO testUserDTO;
    private UserDataDTO testUserDataDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(BigInteger.ONE)
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phone("555-1234")
                .email("john.doe@example.com")
                .baseSalary(50000.0)
                .build();

        testUserDTO = new UserDTO(
                BigInteger.ONE,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "555-1234",
                "john.doe@example.com",
                50000.0
        );

        testUserDataDTO = new UserDataDTO(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "555-1234",
                "john.doe@example.com",
                50000.0
        );
    }

    @Test
    void listenSaveUser_ShouldReturnCreatedResponse() {
        MockServerRequest request = MockServerRequest.builder()
                .body(Mono.just(testUserDataDTO));

        when(userUseCase.existsByEmail("john.doe@example.com")).thenReturn(Mono.just(false));
        when(userDTOMapper.toModel(testUserDataDTO)).thenReturn(testUser);
        when(userUseCase.saveUser(testUser)).thenReturn(Mono.just(testUser));
        when(userDTOMapper.toResponse(testUser)).thenReturn(testUserDTO);

        Mono<ServerResponse> result = handler.listenSaveUser(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CREATED)
                .verifyComplete();
    }

    @Test
    void listenGetUserById_ShouldReturnUser() {
        MockServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "1")
                .build();

        when(userUseCase.getUserById(BigInteger.ONE)).thenReturn(Mono.just(testUser));
        when(userDTOMapper.toResponse(testUser)).thenReturn(testUserDTO);

        Mono<ServerResponse> result = handler.listenGetUserById(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.OK)
                .verifyComplete();
    }

    @Test
    void listenGetUserById_ShouldReturnNotFound() {
        MockServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "999")
                .build();

        when(userUseCase.getUserById(BigInteger.valueOf(999))).thenReturn(Mono.empty());

        Mono<ServerResponse> result = handler.listenGetUserById(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    @Test
    void listenDeleteUserById_ShouldReturnNoContent() {
        MockServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "1")
                .build();

        when(userUseCase.deleteUserById(BigInteger.ONE)).thenReturn(Mono.empty());

        Mono<ServerResponse> result = handler.listenDeleteUserById(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();
    }

    @Test
    void listenGetAllUsers_ShouldReturnAllUsers() {
        MockServerRequest request = MockServerRequest.builder().build();

        User user2 = testUser.toBuilder().id(BigInteger.valueOf(2)).email("jane@example.com").build();
        UserDTO userDTO2 = new UserDTO(BigInteger.valueOf(2), "John", "Doe", 
                LocalDate.of(1990, 1, 1), "123 Main St", "555-1234", "jane@example.com", 50000.0);

        when(userUseCase.findAllUsers()).thenReturn(Flux.just(testUser, user2));
        when(userDTOMapper.toResponseFlux(any(Flux.class))).thenReturn(Flux.just(testUserDTO, userDTO2));

        Mono<ServerResponse> result = handler.listenGetAllUsers(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.OK)
                .verifyComplete();
    }
}