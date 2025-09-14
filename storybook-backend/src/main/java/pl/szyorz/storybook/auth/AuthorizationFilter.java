package pl.szyorz.storybook.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.szyorz.storybook.config.JWTConfig;
import pl.szyorz.storybook.entity.user.DetailsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PropertySource("classpath:application.properties")
@AllArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final DetailsService detailsService;
    private final JWTConfig jwtConfig;


    private final List<AntPathRequestMatcher> anonymousAllowed = List.of(
                new AntPathRequestMatcher( "/swagger-ui/**", "GET"),
                new AntPathRequestMatcher( "/swagger-ui.html", "GET"),
                new AntPathRequestMatcher( "/v3/api-docs/**", "GET"),
                new AntPathRequestMatcher( "/v3/api-docs", "GET"),
                new AntPathRequestMatcher("/auth/**", "POST" ),
                new AntPathRequestMatcher("/api/user/register", "POST"),
                new AntPathRequestMatcher("/api/user/**", "GET"),
                new AntPathRequestMatcher("/api/book/**", "GET"),
                new AntPathRequestMatcher("/api/chapter/**", "GET")
            );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean skipFilter = anonymousAllowed.stream().anyMatch(matcher -> matcher.matches(request));
        if (skipFilter) {
            filterChain.doFilter(request, response);
        } else {
            String authHeader = request.getHeader(AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    String token = authHeader.substring(jwtConfig.getPrefix().length());
                    Algorithm algorithm = Algorithm.HMAC512(jwtConfig.getSecret().getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    UserDetails userDetails = detailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    Map<String, String> error = new HashMap<>();
                    response.setStatus(FORBIDDEN.value());
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                Map<String, String> error = new HashMap<>();
                response.setStatus(FORBIDDEN.value());
                error.put("error_message", "Forbidden Access");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }
}
