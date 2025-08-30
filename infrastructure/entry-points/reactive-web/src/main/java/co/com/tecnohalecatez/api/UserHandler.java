package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.constant.UserConstant;
import co.com.tecnohalecatez.api.dto.ErrorReponseDTO;
import co.com.tecnohalecatez.api.dto.UserDTO;
import co.com.tecnohalecatez.api.dto.UserDataDTO;
import co.com.tecnohalecatez.api.exception.UserDataException;
import co.com.tecnohalecatez.api.exception.UserNotFoundException;
import co.com.tecnohalecatez.api.mapper.UserDTOMapper;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDataDTO.class)
                .flatMap(userDataDTO -> {
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(userDataDTO, UserDTO.class.getName());
                    validator.validate(userDataDTO, errors);
                    if (errors.hasErrors()) {
                        return Mono.error(new UserDataException(UserConstant.INVALID_USER_DATA));
                    }
                    return userUseCase.existsByEmail(userDataDTO.email())
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new UserDataException(UserConstant.INVALID_USER_DATA));
                                }
                                return userUseCase.saveUser(userDTOMapper.toModel(userDataDTO));
                            });
                })
                .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDTOMapper.toResponse(savedUser)))
                .onErrorResume(UserDataException.class, e -> {
                    ErrorReponseDTO errorResponse = new ErrorReponseDTO(
                            Instant.now().toString(),
                            400,
                            "Bad Request",
                            e.getMessage()
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

    public Mono<ServerResponse> listenUpdateUser(ServerRequest serverRequest) {
        return userUseCase.getUserById(new BigInteger(serverRequest.pathVariable("id")))
                .flatMap(existingUser -> serverRequest.bodyToMono(UserDataDTO.class)
                        .flatMap(userDataDTO -> {
                            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(userDataDTO, UserDTO.class.getName());
                            validator.validate(userDataDTO, errors);
                            if (errors.hasErrors()) {
                                return Mono.error(new UserDataException(UserConstant.INVALID_USER_DATA));
                            }
                            return userUseCase.saveUser(userDTOMapper.toModel(userDataDTO).toBuilder().id(existingUser.getId()).build());
                        })
                        .flatMap(updatedUser -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(userDTOMapper.toResponse(updatedUser))
                        )
                )
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")))
                .onErrorResume(UserDataException.class, e -> {
                    ErrorReponseDTO errorResponse = new ErrorReponseDTO(
                            Instant.now().toString(),
                            400,
                            "Bad Request",
                            e.getMessage()
                    );
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(UserNotFoundException.class, e -> {
                    ErrorReponseDTO errorResponse = new ErrorReponseDTO(
                            Instant.now().toString(),
                            404,
                            "Not Found",
                            e.getMessage()
                    );
                    return ServerResponse.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
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
