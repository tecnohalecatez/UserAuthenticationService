package co.com.tecnohalecatez.usecase.user;

import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User testUser;

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
    }

    @Test
    void saveUser_ShouldReturnSavedUser() {
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(testUser));

        Mono<User> result = userUseCase.saveUser(testUser);

        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(BigInteger.ONE)).thenReturn(Mono.just(testUser));

        Mono<User> result = userUseCase.getUserById(BigInteger.ONE);

        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void deleteUserById_ShouldCompleteSuccessfully() {
        when(userRepository.deleteById(BigInteger.ONE)).thenReturn(Mono.empty());

        Mono<Void> result = userUseCase.deleteUserById(BigInteger.ONE);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findAllUsers_ShouldReturnAllUsers() {
        User user2 = testUser.toBuilder().id(BigInteger.valueOf(2)).email("jane@example.com").build();
        when(userRepository.findAll()).thenReturn(Flux.just(testUser, user2));

        Flux<User> result = userUseCase.findAllUsers();

        StepVerifier.create(result)
                .expectNext(testUser)
                .expectNext(user2)
                .verifyComplete();
    }

    @Test
    void existsByEmail_ShouldReturnTrue() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(Mono.just(true));

        Mono<Boolean> result = userUseCase.existsByEmail("john.doe@example.com");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByEmail_ShouldReturnFalse() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(Mono.just(false));

        Mono<Boolean> result = userUseCase.existsByEmail("nonexistent@example.com");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}