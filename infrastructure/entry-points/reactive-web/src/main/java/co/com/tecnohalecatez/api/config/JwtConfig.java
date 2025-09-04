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

    private static final String SECRET_BASE64 = "UHJ1ZWJhQXBpQXV0ZW50aWNhY2lvblRlY25vaGFsZWNhdGV6";

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_BASE64));
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
