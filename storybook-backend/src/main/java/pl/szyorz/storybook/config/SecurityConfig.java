package pl.szyorz.storybook.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.szyorz.storybook.auth.AuthenticationFilter;
import pl.szyorz.storybook.auth.AuthorizationFilter;
import pl.szyorz.storybook.entity.user.DetailsService;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig{
    private DetailsService detailsService;
    private JWTConfig jwtConfig;
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public UserDetailsService userDetailsService() {
        return detailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(jwtConfig, authenticationManagerBean(authenticationConfiguration));
        authenticationFilter.setFilterProcessesUrl("/auth");

        AuthorizationFilter authorizationFilter = new AuthorizationFilter(detailsService, jwtConfig);

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(webConfigurationSource()))
                .authorizeHttpRequests(auth ->
                    auth
                            .requestMatchers("/api/**", "/api/book/**", "/api/user/register").permitAll()
                            .requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll()
                            .anyRequest().denyAll()

                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilter(authenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    UrlBasedCorsConfigurationSource webConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    private final List<RequestMatcher> anonymousAllowed = List.of(
//            new AntPathRequestMatcher( "/swagger-ui/**", "GET"),
//            new AntPathRequestMatcher( "/swagger-ui.html", "GET"),
//            new AntPathRequestMatcher( "/v3/api-docs/**", "GET"),
//            new AntPathRequestMatcher( "/v3/api-docs", "GET"),
//            new AntPathRequestMatcher("/auth", "POST"),
//            new AntPathRequestMatcher("/auth/**", "POST" ),
//            new AntPathRequestMatcher("/api/user/register", "POST"),
//            new AntPathRequestMatcher("/api/user/**", "GET"),
//            new AntPathRequestMatcher("/api/book/**", "GET")
//    );
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers(anonymousAllowed.toArray(new RequestMatcher[1]));
//    }


}
