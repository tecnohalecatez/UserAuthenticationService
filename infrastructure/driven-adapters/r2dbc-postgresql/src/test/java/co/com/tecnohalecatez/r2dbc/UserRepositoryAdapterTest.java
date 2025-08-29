package co.com.tecnohalecatez.r2dbc;

import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter repositoryAdapter;

    private User testUser;
    private UserEntity testUserEntity;

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

        testUserEntity = UserEntity.builder()
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
    void save_ShouldReturnSavedUser() {
        when(mapper.map(testUser, UserEntity.class)).thenReturn(testUserEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(testUserEntity));
        when(mapper.map(testUserEntity, User.class)).thenReturn(testUser);

        Mono<User> result = repositoryAdapter.save(testUser);

        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnUser() {
        when(repository.findById(BigInteger.ONE)).thenReturn(Mono.just(testUserEntity));
        when(mapper.map(testUserEntity, User.class)).thenReturn(testUser);

        Mono<User> result = repositoryAdapter.findById(BigInteger.ONE);

        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void deleteById_ShouldCompleteSuccessfully() {
        when(repository.deleteById(BigInteger.ONE)).thenReturn(Mono.empty());

        Mono<Void> result = repositoryAdapter.deleteById(BigInteger.ONE);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(BigInteger.valueOf(2));
        userEntity2.setEmail("jane@example.com");
        
        User user2 = testUser.toBuilder().id(BigInteger.valueOf(2)).email("jane@example.com").build();

        when(repository.findAll()).thenReturn(Flux.just(testUserEntity, userEntity2));
        when(mapper.map(testUserEntity, User.class)).thenReturn(testUser);
        when(mapper.map(userEntity2, User.class)).thenReturn(user2);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(testUser)
                .expectNext(user2)
                .verifyComplete();
    }

    @Test
    void existsByEmail_ShouldReturnTrue() {
        when(repository.existsByEmail("john.doe@example.com")).thenReturn(Mono.just(true));

        Mono<Boolean> result = repositoryAdapter.existsByEmail("john.doe@example.com");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByEmail_ShouldReturnFalse() {
        when(repository.existsByEmail("nonexistent@example.com")).thenReturn(Mono.just(false));

        Mono<Boolean> result = repositoryAdapter.existsByEmail("nonexistent@example.com");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnEmpty() {
        when(repository.findById(BigInteger.valueOf(999))).thenReturn(Mono.empty());

        Mono<User> result = repositoryAdapter.findById(BigInteger.valueOf(999));

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findAll_ShouldReturnEmptyFlux() {
        when(repository.findAll()).thenReturn(Flux.empty());

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void save_ShouldHandleError() {
        when(mapper.map(testUser, UserEntity.class)).thenReturn(testUserEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<User> result = repositoryAdapter.save(testUser);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
