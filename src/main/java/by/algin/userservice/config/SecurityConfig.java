package by.algin.userservice.config;

import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.constants.RoleConstants;
import by.algin.userservice.security.JwtAuthenticationEntryPoint;
import by.algin.userservice.security.JwtAuthenticationFilter;
import by.algin.userservice.security.JwtAuthenticationSuccessHandler;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                PathConstants.ROOT,
                                PathConstants.AUTH_LOGIN,
                                PathConstants.AUTH_LOGOUT,
                                PathConstants.AUTH_REGISTER,
                                PathConstants.AUTH_REGISTRATION_SUCCESS,
                                PathConstants.AUTH_CONFIRM,
                                PathConstants.AUTH_TOKEN_EXPIRED,
                                PathConstants.AUTH_RESEND_CONFIRMATION
                        ).permitAll()
                        .requestMatchers(PathConstants.CSS, PathConstants.JS, PathConstants.IMAGES).permitAll()
                        .requestMatchers(PathConstants.ADMIN_DASHBOARD).hasAuthority(RoleConstants.ADMIN)
                        .requestMatchers(PathConstants.DASHBOARD).authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(PathConstants.AUTH_LOGIN)
                        .loginProcessingUrl(PathConstants.AUTH_LOGIN)
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(PathConstants.AUTH_LOGOUT)
                        .logoutSuccessUrl(PathConstants.ROOT)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler();
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