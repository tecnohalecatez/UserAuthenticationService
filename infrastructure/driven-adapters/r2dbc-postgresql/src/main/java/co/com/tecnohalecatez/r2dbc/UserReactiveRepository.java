package co.com.tecnohalecatez.r2dbc;

import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UserReactiveRepository
        extends ReactiveCrudRepository<UserEntity, BigInteger>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<Boolean> existsByEmail(String email);
}
