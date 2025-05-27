package by.algin.userservice.service;

import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.exception.InvalidTokenException;
import by.algin.userservice.dto.request.LoginRequest;
import by.algin.userservice.dto.request.TokenRefreshRequest;
import by.algin.userservice.dto.request.TokenValidationRequest;
import by.algin.userservice.dto.response.ApiResponse;
import by.algin.userservice.dto.response.AuthResponse;
import by.algin.userservice.dto.response.TokenValidationResponse;
import by.algin.userservice.entity.User;
import by.algin.userservice.exception.AccountDisabledException;
import by.algin.userservice.exception.InvalidCredentialsException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;

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
            Long expiresIn = jwtService.getClaimsFromToken(accessToken).getExpiration().getTime();

            AuthResponse authResponse = authMapper.toAuthResponse(user, accessToken, refreshToken, expiresIn);

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
        Long expiresIn = jwtService.getClaimsFromToken(accessToken).getExpiration().getTime();

        AuthResponse authResponse = authMapper.toAuthResponse(
                user,
                accessToken,
                refreshRequest.getRefreshToken(),
                expiresIn
        );
        log.info("Token refreshed successfully for user: {}", user.getUsername());
        return new ApiResponse<>(true, "Token refreshed successfully", authResponse);
    }

    public ApiResponse<TokenValidationResponse> validateToken(TokenValidationRequest validationRequest) {
        log.info("Validating token");

        if (!jwtService.validateToken(validationRequest.getToken())) {
            log.warn("Invalid token provided");
            TokenValidationResponse invalidResponse = authMapper.toInvalidTokenResponse("Invalid token");
            return new ApiResponse<>(false, "Invalid token", invalidResponse);
        }
        String username = jwtService.getUsernameFromToken(validationRequest.getToken());
        Claims claims = jwtService.getClaimsFromToken(validationRequest.getToken());
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        if (!user.isEnabled()) {
            log.warn("Token is valid but user is disabled: {}", username);
            TokenValidationResponse disabledResponse = authMapper.toInvalidTokenResponse("User is disabled");
            return new ApiResponse<>(false, "User is disabled", disabledResponse);
        }
        TokenValidationResponse validationResponse = authMapper.toTokenValidationResponse(user, claims, true);
        log.info("Token validated successfully for user: {}", username);
        return new ApiResponse<>(true, "Token is valid", validationResponse);
    }
}