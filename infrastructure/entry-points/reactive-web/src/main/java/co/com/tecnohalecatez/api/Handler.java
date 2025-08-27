package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.dto.ErrorReponseDTO;
import co.com.tecnohalecatez.api.dto.UserDTO;
import co.com.tecnohalecatez.api.mapper.UserDTOMapper;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(userDTO -> {
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(userDTO, UserDTO.class.getName());
                    validator.validate(userDTO, errors);
                    if (errors.hasErrors()) {
                        return Mono.error(new BindException(errors));
                    }
                    return userUseCase.saveUser(userDTOMapper.toModel(userDTO));
                })
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDTOMapper.toResponse(savedUser)))
                .onErrorResume(BindException.class, e -> {
                    ErrorReponseDTO errorResponse = new ErrorReponseDTO(
                            Instant.now().toString(),
                            400,
                            "Invalid user input data",
                            serverRequest.path(),
                            e.getBindingResult().getAllErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .collect(Collectors.toList())
                    );
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest serverRequest) {
        return userUseCase.getUserById(new BigInteger(serverRequest.pathVariable("id")))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDTOMapper.toResponse(user)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenUpdateUser(@Valid ServerRequest serverRequest) {
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
