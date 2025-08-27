package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.config.UserPath;
import co.com.tecnohalecatez.api.dto.ErrorReponseDTO;
import co.com.tecnohalecatez.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Register a new user",
                            description = "Receives a CreateUserDTO object and saves a new user in the system.",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User creation data",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User successfully registered",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid user input data",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Conflict: Email or identification already exists",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/{id}",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetUserById",
                    operation = @Operation(
                            operationId = "getUserById",
                            summary = "Get user by ID",
                            description = "Retrieve a user by their unique identifier.",
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "The unique ID of the user",
                                            required = true,
                                            in = ParameterIn.PATH,
                                            schema = @Schema(type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User found",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "User not found",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid ID supplied",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = {"application/json"},
                    method = RequestMethod.PUT,
                    beanClass = Handler.class,
                    beanMethod = "listenUpdateUser",
                    operation = @Operation(
                            operationId = "updateUser",
                            summary = "Update user",
                            description = "Updates an existing user using the user data.",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User data to update",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User successfully updated",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid user data supplied",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "User not found",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/{id}",
                    produces = {"application/json"},
                    method = RequestMethod.DELETE,
                    beanClass = Handler.class,
                    beanMethod = "listenDeleteUserById",
                    operation = @Operation(
                            operationId = "deleteUserById",
                            summary = "Delete user by ID",
                            description = "Deletes a user identified by the given unique ID.",
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "The unique ID of the user to delete",
                                            required = true,
                                            in = ParameterIn.PATH,
                                            schema = @Schema(type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "User successfully deleted",
                                            content = @Content
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "User not found",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid ID supplied",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetAllUsers",
                    operation = @Operation(
                            operationId = "getAllUsers",
                            summary = "Get all users",
                            description = "Retrieve a list of all users from the system.",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of users retrieved successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorReponseDTO.class)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler, UserPath userPath) {
        return route(POST(userPath.getUsers()), handler::listenSaveUser)
                .andRoute(GET(userPath.getUsersById()), handler::listenGetUserById)
                .andRoute(PUT(userPath.getUsers()), handler::listenUpdateUser)
                .andRoute(DELETE(userPath.getUsersById()), handler::listenDeleteUserById)
                .andRoute(GET(userPath.getUsers()), handler::listenGetAllUsers);
    }

}
