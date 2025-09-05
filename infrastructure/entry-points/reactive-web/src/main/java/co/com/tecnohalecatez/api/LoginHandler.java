package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.constant.UserConstant;
import co.com.tecnohalecatez.api.dto.LoginDTO;
import co.com.tecnohalecatez.api.dto.LoginDataDTO;
import co.com.tecnohalecatez.api.exception.UserDataException;
import co.com.tecnohalecatez.usecase.role.RoleUseCase;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import co.com.tecnohalecatez.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginHandler {

    private final UserUseCase userUseCase;
    private final RoleUseCase roleUseCase;
    private final Validator validator;

    public Mono<ServerResponse> listenGetToken(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginDataDTO.class)
                .flatMap(loginDataDTO -> {
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(loginDataDTO, LoginDataDTO.class.getName());
                    validator.validate(loginDataDTO, errors);
                    if (errors.hasErrors()) {
                        return Mono.error(new UserDataException(UserConstant.INVALID_USER_DATA));
                    }
                    return userUseCase.existsByEmail(loginDataDTO.email())
                            .flatMap(exists -> {
                                if (Boolean.FALSE.equals(exists)) {
                                    return Mono.error(new UserDataException(UserConstant.INVALID_USER_DATA));
                                }
                                return userUseCase.getUserByEmail(loginDataDTO.email())
                                        .flatMap(user -> {
                                                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                                                    if (!passwordEncoder.matches(loginDataDTO.password(), user.getPassword())) {
                                                        return Mono.error(new UserDataException(UserConstant.INVALID_USER_DATA));
                                                    }
                                                    return roleUseCase.getRoleById(user.getRoleId())
                                                            .flatMap(role ->
                                                                    Mono.fromCallable(() -> JwtUtil.generateToken(user.getEmail(), role.getName()))
                                                                            .subscribeOn(Schedulers.boundedElastic())
                                                                            .flatMap(token -> ServerResponse.status(HttpStatus.OK)
                                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                                    .bodyValue(new LoginDTO(token))
                                                                            )
                                                            );
                                                }
                                        );
                            });
                });
    }
}
