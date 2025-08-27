package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.dto.UserDTO;
import co.com.tecnohalecatez.api.mapper.UserDTOMapper;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(userDTO -> userUseCase.saveUser(userDTOMapper.toModel(userDTO)))
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDTOMapper.toResponse(savedUser)));
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest serverRequest) {
        return userUseCase.getUserById(new BigInteger(serverRequest.pathVariable("id")))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDTOMapper.toResponse(user)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenUpdateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(userDTO -> userUseCase.updateUser(userDTOMapper.toModel(userDTO)))
                .flatMap(updatedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDTOMapper.toResponse(updatedUser)));
    }

    public Mono<ServerResponse> listenDeleteUserById(ServerRequest serverRequest) {
        return userUseCase.deleteUserById(new BigInteger(serverRequest.pathVariable("id")))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTOMapper.toResponseFlux(userUseCase.findAllUsers()), UserDTO.class);
    }

}
