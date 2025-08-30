package co.com.tecnohalecatez.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Authentication Service API")
                        .description("REST API for user management with clean architecture")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("TecnoHalecatez Team")
                                .email("contacto@tecnohalecatez.com.co")
                                .url("https://tecnohalecatez.com.co"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9090")
                                .description("Development Server")
                ));
    }
}