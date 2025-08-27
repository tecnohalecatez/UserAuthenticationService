package co.com.tecnohalecatez.model.user.gateways;

import co.com.tecnohalecatez.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigInteger;

public interface UserRepository {
    Mono<User> save(User user);

    Mono<User> findById(BigInteger id);

    Mono<User> update(User user);

    Mono<Void> deleteById(BigInteger id);

    Flux<User> findAll();


}
