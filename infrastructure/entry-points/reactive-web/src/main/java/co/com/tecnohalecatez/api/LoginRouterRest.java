package co.com.tecnohalecatez.api;

import co.com.tecnohalecatez.api.config.LoginPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouterRest {
    @Bean("loginRouterFunction")
    public RouterFunction<ServerResponse> routerFunction(LoginHandler loginHandler, LoginPath loginPath) {
        return route(POST(loginPath.getLogin()), loginHandler::listenGetToken);
    }
}
