package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.config.UserPath;
import co.com.tecnohalecatez.api.constant.UserConstant;
import co.com.tecnohalecatez.api.dto.ErrorResponseDTO;
import co.com.tecnohalecatez.api.dto.UserDTO;
import co.com.tecnohalecatez.api.dto.UserDataDTO;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = UserConstant.BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = UserConstant.LISTEN + UserConstant.SAVE_USER,
                    operation = @Operation(
                            operationId = UserConstant.SAVE_USER,
                            summary = "🆕  Register a new user",
                            description = "Receives a UserDataDTO object and saves a new user in the system.",
                            tags = {"Users"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User creation data",
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = UserDataDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User successfully registered",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = UserDTO.class)
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
            ),
            @RouterOperation(
                    path = UserConstant.PATH_ID,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = UserConstant.LISTEN + UserConstant.GET_USER_ID,
                    operation = @Operation(
                            operationId = UserConstant.GET_USER_ID,
                            summary = "🔍 Get user by ID",
                            description = "Retrieve a user by their unique identifier.",
                            tags = {"Users"},
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
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid ID supplied",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "User not found",
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
            ),
            @RouterOperation(
                    path = UserConstant.BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanClass = UserHandler.class,
                    beanMethod = UserConstant.LISTEN + UserConstant.UPDATE_USER,
                    operation = @Operation(
                            operationId = UserConstant.UPDATE_USER,
                            summary = "✏️ Update user",
                            description = "Updates an existing user using the user data.",
                            tags = {"Users"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User data to update",
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = UserDataDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User successfully updated",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid user data supplied",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "User not found",
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
            ),
            @RouterOperation(
                    path = UserConstant.PATH_ID,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.DELETE,
                    beanClass = UserHandler.class,
                    beanMethod = UserConstant.LISTEN + UserConstant.DELETE_USER_ID,
                    operation = @Operation(
                            operationId = UserConstant.DELETE_USER_ID,
                            summary = "🗑️ Delete user by ID",
                            description = "Deletes a user identified by the given unique ID.",
                            tags = {"Users"},
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
                                            responseCode = "400",
                                            description = "Invalid ID supplied",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "User not found",
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
            ),
            @RouterOperation(
                    path = UserConstant.BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = UserConstant.LISTEN + UserConstant.GET_ALL_USERS,
                    operation = @Operation(
                            operationId = UserConstant.GET_ALL_USERS,
                            summary = "📄 Get all users",
                            description = "Retrieve a list of all users from the system.",
                            tags = {"Users"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of users retrieved successfully",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))
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
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler, UserPath userPath) {
        return route(POST(userPath.getUsers()), userHandler::listenSaveUser)
                .andRoute(GET(userPath.getUsersById()), userHandler::listenGetUserById)
                .andRoute(PUT(userPath.getUsersById()), userHandler::listenUpdateUser)
                .andRoute(DELETE(userPath.getUsersById()), userHandler::listenDeleteUserById)
                .andRoute(GET(userPath.getUsers()), userHandler::listenGetAllUsers);
    }

}
