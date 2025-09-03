package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.constant.UserConstant;
import co.com.tecnohalecatez.api.dto.LoginDTO;
import co.com.tecnohalecatez.api.dto.LoginDataDTO;
import co.com.tecnohalecatez.api.exception.UserDataException;
import co.com.tecnohalecatez.usecase.user.UserUseCase;
import co.com.tecnohalecatez.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoginHandler {

    private final UserUseCase userUseCase;
    private final Validator validator;

    public Mono<ServerResponse> listenGetToken(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginDataDTO.class)
                .flatMap(loginDataDTO -> {
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(loginDataDTO, LoginDataDTO.class.getName());
                    validator.validate(loginDataDTO, errors);
                    if (errors.hasErrors()) {
                        return Mono.error(new UserDataException(UserConstant.INVALID_USER_DATA));
                    }
                    return userUseCase.existsByEmailAndPassword(loginDataDTO.email(), loginDataDTO.password())
                            .flatMap(exists -> {
                                if (Boolean.FALSE.equals(exists)) {
                                    return Mono.error(new UserDataException(UserConstant.INVALID_USER_DATA));
                                }
                                return userUseCase.findByEmailAndPassword(loginDataDTO.email(), loginDataDTO.password())
                                        .flatMap(user -> {
                                            String token = JwtUtil.generateToken(user.getEmail(), String.valueOf(user.getRoleId()));
                                            return ServerResponse.status(HttpStatus.OK)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .bodyValue(new LoginDTO(token));
                                        });
                            });
                });
    }
}
