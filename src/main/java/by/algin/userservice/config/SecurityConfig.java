package by.algin.userservice.config;

import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.security.JwtAuthenticationEntryPoint;
import by.algin.userservice.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(PathConstants.API)
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
.authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathConstants.API_AUTH).permitAll()
                        .requestMatchers(PathConstants.API_USERS_SEARCH).authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathConstants.AUTH_LOGIN).permitAll()
                        .requestMatchers(PathConstants.AUTH_REGISTER).permitAll()
                        .requestMatchers(PathConstants.AUTH_REGISTRATION_SUCCESS).permitAll()
                        .requestMatchers(PathConstants.AUTH_CONFIRM + "**").permitAll()
                        .requestMatchers(PathConstants.AUTH_TOKEN_EXPIRED).permitAll()
                        .requestMatchers(PathConstants.AUTH_RESEND_CONFIRMATION).permitAll()
                        .requestMatchers(PathConstants.CSS).permitAll()
                        .requestMatchers(PathConstants.JS).permitAll()
                        .requestMatchers(PathConstants.IMAGES).permitAll()
                        .requestMatchers(PathConstants.ROOT).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(PathConstants.AUTH_LOGIN)
                        .defaultSuccessUrl(PathConstants.DASHBOARD, true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(PathConstants.AUTH_LOGOUT)
                        .logoutSuccessUrl(PathConstants.AUTH_LOGIN)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}