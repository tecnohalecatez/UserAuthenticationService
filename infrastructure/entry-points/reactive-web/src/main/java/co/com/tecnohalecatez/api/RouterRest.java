package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.config.UserPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler, UserPath userPath) {
        return route(POST(userPath.getUsers()), handler::listenSaveUser)
                .andRoute(GET(userPath.getUsersById()), handler::listenGetUserById)
                .andRoute(PUT(userPath.getUsers()), handler::listenUpdateUser)
                .andRoute(DELETE(userPath.getUsersById()), handler::listenDeleteUserById)
                .andRoute(GET(userPath.getUsers()), handler::listenGetAllUsers);
    }
}
