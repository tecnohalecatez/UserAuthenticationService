package co.com.tecnohalecatez.usecase.user;

import co.com.tecnohalecatez.model.user.User;
import co.com.tecnohalecatez.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> getUserById(BigInteger id) {
        return userRepository.findById(id);
    }

    public Mono<User> updateUser(User user) {
        return userRepository.update(user);
    }

    public Mono<Void> deleteUserById(BigInteger id) {
        return userRepository.deleteById(id);
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAll();
    }

}
