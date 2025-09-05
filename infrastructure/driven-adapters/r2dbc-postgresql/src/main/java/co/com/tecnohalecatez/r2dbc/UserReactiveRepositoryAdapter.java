package co.com.tecnohalecatez.r2dbc;

import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.model.user.gateways.UserRepository;
import co.com.tecnohalecatez.r2dbc.entity.UserEntity;
import co.com.tecnohalecatez.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Slf4j
@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity,
        BigInteger, UserReactiveRepository> implements UserRepository {

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

    @Override
    @Transactional
    public Mono<User> save(User user) {
        log.trace("Start save user: {}", user.toString());
        return super.save(user)
                .doOnSuccess(u -> log.trace("User saved: {}", u.toString()))
                .doOnError(e -> log.error("Error saving user: {}", e.getMessage()));
    }

    @Override
    public Mono<User> findById(BigInteger id) {
        log.trace("Start find user by id = {}", id);
        return super.findById(id);
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(BigInteger id) {
        log.trace("Start delete user with id = {}", id);
        return repository.deleteById(id)
                .doOnSuccess(u -> log.trace("User deleted with id = {}", id))
                .doOnError(e -> log.error("Error deleting user: {}", e.getMessage()));
    }

    @Override
    public Flux<User> findAll() {
        log.trace("Start find all users");
        return super.findAll();
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        log.trace("Start checking existence of user by email = {}", email);
        return repository.existsByEmail(email)
                .doOnSuccess(exists -> log.trace("Existence check for email = {} -> {}", email, exists))
                .doOnError(e -> log.error("Error checking existence of email = {} -> {}", email, e.getMessage(), e));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        log.trace("Start find user by email = {} ", email);
        return repository.findByEmail(email)
                .map(this::toEntity)
                .doOnSuccess(user -> log.trace("User found by email = {} -> {}", email, user.toString()))
                .doOnError(e -> log.error("Error finding user by email = {} -> {}", email, e.getMessage(), e));
    }

}
