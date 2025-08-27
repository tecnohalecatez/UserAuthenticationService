package co.com.tecnohalecatez.r2dbc;

import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.model.user.gateways.UserRepository;
import co.com.tecnohalecatez.r2dbc.entity.UserEntity;
import co.com.tecnohalecatez.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity,
        BigInteger, UserReactiveRepository> implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryAdapter.class);

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

    @Override
    @Transactional
    public Mono<User> save(User user) {
        LOGGER.trace("Start save user: {}", user.toString());
        return super.save(user)
                .doOnSuccess(u -> LOGGER.trace("User saved: {}", u.toString()))
                .doOnError(e -> LOGGER.error("Error saving user: {}", e.getMessage()));
    }

    @Override
    public Mono<User> findById(BigInteger id) {
        LOGGER.trace("Start find user by id = {}", id);
        return super.findById(id);
    }

    @Override
    @Transactional
    public Mono<User> update(User user) {
        LOGGER.trace("Start update user with id = {}", user.getId());
        return super.findById(user.getId())
                .doOnNext(u -> LOGGER.debug("User found: {}", u.toString()))
                .switchIfEmpty(Mono.error(new Throwable("User not found")))
                .flatMap(existingUser -> super.save(existingUser.toBuilder()
                        .name(user.getName())
                        .surname(user.getSurname())
                        .birthDate(user.getBirthDate())
                        .address(user.getAddress())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .baseSalary(user.getBaseSalary())
                        .build()))
                .doOnSuccess(u -> LOGGER.trace("User updated: {}", u.toString()))
                .doOnError(e -> LOGGER.error("Error updating user: {}", e.getMessage()));
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(BigInteger id) {
        LOGGER.trace("Start delete user with id = {}", id);
        return repository.deleteById(id)
                .doOnSuccess(u -> LOGGER.trace("User deleted with id = {}", id))
                .doOnError(e -> LOGGER.error("Error deleting user: {}", e.getMessage()));
    }

    @Override
    public Flux<User> findAll() {
        LOGGER.trace("Start find all users");
        return super.findAll();
    }

}
