package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.config.LoginPath;
import co.com.tecnohalecatez.api.constant.LoginConstant;
import co.com.tecnohalecatez.api.constant.UserConstant;
import co.com.tecnohalecatez.api.dto.ErrorResponseDTO;
import co.com.tecnohalecatez.api.dto.LoginDTO;
import co.com.tecnohalecatez.api.dto.LoginDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouterRest {

    @Bean("loginRouterFunction")
    @RouterOperations({
            @RouterOperation(
                    path = LoginConstant.BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = LoginHandler.class,
                    beanMethod = LoginConstant.LISTEN + LoginConstant.GET_TOKEN,
                    operation = @Operation(
                            operationId = LoginConstant.GET_TOKEN,
                            summary = "🔑 Authenticate user and generate JWT token",
                            description = "Authenticates user with email and password, returns JWT token with role information if credentials are valid.",
                            tags = {"Login"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User login credentials",
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = LoginDataDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Authentication successful, JWT token generated",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = LoginDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = UserConstant.INVALID_USER_DATA,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = UserConstant.INTERNAL_SERVER_ERROR,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDTO.class)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(LoginHandler loginHandler, LoginPath loginPath) {
        return route(POST(loginPath.getLogin()), loginHandler::listenGetToken);
    }
}
