package by.algin.userservice.service;

import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.exception.InvalidTokenException;
import by.algin.userservice.DTO.request.LoginRequest;
import by.algin.userservice.DTO.request.TokenRefreshRequest;
import by.algin.userservice.DTO.request.TokenValidationRequest;
import by.algin.userservice.DTO.response.ApiResponse;
import by.algin.userservice.DTO.response.AuthResponse;
import by.algin.userservice.DTO.response.TokenValidationResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import by.algin.userservice.exception.AccountDisabledException;
import by.algin.userservice.exception.InvalidCredentialsException;
import by.algin.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public ApiResponse<AuthResponse> login(LoginRequest loginRequest) {
        log.info("Attempting login for user: {}", loginRequest.getUsernameOrEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsernameOrEmail(
                    userDetails.getUsername(), userDetails.getUsername()
            ).orElseThrow(UserNotFoundException::new);

            if (!user.isEnabled()) {
                throw new AccountDisabledException();
            }

            String accessToken = jwtService.generateAccessToken(authentication);
            String refreshToken = jwtService.generateRefreshToken(authentication);

            Set<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getClaimsFromToken(accessToken).getExpiration().getTime())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .roles(roles)
                    .build();

            log.info("User logged in successfully: {}", user.getUsername());

            return new ApiResponse<>(true, "Login successful", authResponse);

        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", loginRequest.getUsernameOrEmail());
            throw new InvalidCredentialsException();
        } catch (DisabledException e) {
            log.error("Account disabled for user: {}", loginRequest.getUsernameOrEmail());
            throw new AccountDisabledException();
        }
    }

    public ApiResponse<AuthResponse> refreshToken(TokenRefreshRequest refreshRequest) {
        log.info("Refreshing token");

        if (!jwtService.validateToken(refreshRequest.getRefreshToken())) {
            throw new InvalidTokenException();
        }

        String username = jwtService.getUsernameFromToken(refreshRequest.getRefreshToken());

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            throw new AccountDisabledException();
        }

        String accessToken = jwtService.generateAccessToken(user);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshRequest.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getClaimsFromToken(accessToken).getExpiration().getTime())
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();

        log.info("Token refreshed successfully for user: {}", user.getUsername());

        return new ApiResponse<>(true, "Token refreshed successfully", authResponse);
    }

    public ApiResponse<TokenValidationResponse> validateToken(TokenValidationRequest validationRequest) {
        log.info("Validating token");

        if (!jwtService.validateToken(validationRequest.getToken())) {
            log.warn("Invalid token provided");
            return new ApiResponse<>(false, "Invalid token",
                    TokenValidationResponse.builder().valid(false).build());
        }

        String username = jwtService.getUsernameFromToken(validationRequest.getToken());
        Claims claims = jwtService.getClaimsFromToken(validationRequest.getToken());

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            log.warn("Token is valid but user is disabled: {}", username);
            return new ApiResponse<>(false, "User is disabled",
                    TokenValidationResponse.builder().valid(false).build());
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Map<String, Object> customClaims = new HashMap<>(claims);
        customClaims.remove("sub");
        customClaims.remove("iat");
        customClaims.remove("exp");

        TokenValidationResponse validationResponse = TokenValidationResponse.builder()
                .valid(true)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .claims(customClaims)
                .build();

        log.info("Token validated successfully for user: {}", username);

        return new ApiResponse<>(true, "Token is valid", validationResponse);
    }
}