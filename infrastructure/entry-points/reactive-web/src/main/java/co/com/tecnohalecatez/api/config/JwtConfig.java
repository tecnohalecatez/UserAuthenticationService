package co.com.tecnohalecatez.api.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
public class JwtConfig {

    private static final String SECRET_BASE64 = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_BASE64));
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
