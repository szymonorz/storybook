package pl.szyorz.storybook;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.szyorz.storybook.config.JWTConfig;

import java.util.Date;

public class SecurityTestUtils {
    private final ObjectMapper mapper = new ObjectMapper();

    public static String jwtFor(JWTConfig config, String username) {
        Algorithm alg = Algorithm.HMAC512(config.getSecret().getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60_000)) // 1 min
                .withIssuer("test")
                .sign(alg);
    }
}
