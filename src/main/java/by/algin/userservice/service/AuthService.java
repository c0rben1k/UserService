package by.algin.userservice.service;

import by.algin.dto.request.LoginRequest;
import by.algin.dto.request.TokenRefreshRequest;
import by.algin.dto.request.TokenValidationRequest;
import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.AuthResponse;
import by.algin.dto.response.TokenValidationResponse;
import by.algin.userservice.entity.User;
import by.algin.userservice.exception.AccountDisabledException;
import by.algin.userservice.exception.InvalidCredentialsException;
import by.algin.userservice.exception.InvalidTokenException;
import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.mapper.AuthMapper;
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
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;

    public ApiResponse<AuthResponse> login(LoginRequest loginRequest) {
        log.info("Processing login request for user: {}", loginRequest.getUsernameOrEmail());

        try {
            Authentication authentication = authenticateUser(loginRequest);
            User user = findUserByAuthentication(authentication);

            validateUserAccount(user);
            logUserRoles(user);

            AuthResponse authResponse = createAuthResponse(authentication, user);

            log.info("Login successful for user: {}", user.getUsername());
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
        log.info("Processing token refresh request");

        String refreshToken = refreshRequest.getRefreshToken();
        if (!StringUtils.hasText(refreshToken)) {
            log.error("Refresh token is null or empty");
            throw new InvalidTokenException("Refresh token cannot be null or empty");
        }

        validateRefreshToken(refreshToken);

        String username = jwtService.getUsernameFromToken(refreshToken);
        User user = findUserByUsername(username);

        validateUserAccount(user);

        AuthResponse authResponse = createRefreshAuthResponse(user, refreshToken);

        log.info("Token refreshed successfully for user: {}", user.getUsername());
        return new ApiResponse<>(true, "Token refreshed successfully", authResponse);
    }

    public ApiResponse<TokenValidationResponse> validateToken(TokenValidationRequest validationRequest) {
        log.info("Processing token validation request");

        if (!jwtService.validateToken(validationRequest.getToken())) {
            log.warn("Invalid token provided");
            return createInvalidTokenResponse("Invalid token");
        }

        String username = jwtService.getUsernameFromToken(validationRequest.getToken());
        Claims claims = jwtService.getAllClaimsFromToken(validationRequest.getToken());
        User user = findUserByUsername(username);

        if (!user.isEnabled()) {
            log.warn("Token is valid but user is disabled: {}", username);
            return createInvalidTokenResponse("User is disabled");
        }

        TokenValidationResponse validationResponse = authMapper.toTokenValidationResponse(user, claims, true);
        log.info("Token validated successfully for user: {}", username);
        return new ApiResponse<>(true, "Token is valid", validationResponse);
    }

    private Authentication authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private User findUserByAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateUserAccount(User user) {
        if (!user.isEnabled()) {
            throw new AccountDisabledException();
        }
    }

    private void validateRefreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            log.error("Invalid refresh token format or signature");
            throw new InvalidTokenException("Invalid refresh token format or signature");
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            log.error("Token is not a refresh token");
            throw new InvalidTokenException("Token is not a refresh token");
        }
    }

    private void logUserRoles(User user) {
        log.info("User found: {}", user.getUsername());
        log.info("User roles before mapping: {}", user.getRoles());
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> log.info("Role: {}", role.getName()));
        }
    }

    private AuthResponse createAuthResponse(Authentication authentication, User user) {
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);
        Long expiresIn = jwtService.getAllClaimsFromToken(accessToken).getExpiration().getTime() / 1000;

        AuthResponse authResponse = authMapper.toAuthResponse(user, accessToken, refreshToken, expiresIn);
        log.info("AuthResponse roles: {}", authResponse.getRoles());

        return authResponse;
    }

    private AuthResponse createRefreshAuthResponse(User user, String refreshToken) {
        String accessToken = jwtService.generateAccessToken(user);
        Long expiresIn = jwtService.getAllClaimsFromToken(accessToken).getExpiration().getTime() / 1000;

        return authMapper.toAuthResponse(user, accessToken, refreshToken, expiresIn);
    }

    private ApiResponse<TokenValidationResponse> createInvalidTokenResponse(String message) {
        TokenValidationResponse invalidResponse = authMapper.toInvalidTokenResponse(message);
        return new ApiResponse<>(false, message, invalidResponse);
    }
}