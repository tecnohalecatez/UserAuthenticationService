package co.com.tecnohalecatez.r2dbc;

import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.model.user.gateways.UserRepository;
import co.com.tecnohalecatez.r2dbc.entity.UserEntity;
import co.com.tecnohalecatez.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        BigInteger,
        UserReactiveRepository
        > implements UserRepository {
    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user);
    }

    @Override
    public Mono<User> findById(BigInteger id) {
        return super.findById(id);
    }

    @Override
    public Mono<User> update(User user) {
        return super.findById(user.getId())
                .switchIfEmpty(Mono.error(new Throwable("User not found")))
                .flatMap(existingUser -> super.save(existingUser.toBuilder()
                        .name(user.getName())
                        .surname(user.getSurname())
                        .birthDate(user.getBirthDate())
                        .address(user.getAddress())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .baseSalary(user.getBaseSalary())
                        .build()));
    }

    @Override
    public Mono<Void> deleteById(BigInteger id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<User> findAll() {
        return super.findAll();
    }

}
