package by.algin.userservice.service;

import by.algin.dto.request.LoginRequest;
import by.algin.dto.request.TokenRefreshRequest;
import by.algin.dto.request.TokenValidationRequest;
import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.AuthResponse;
import by.algin.dto.response.TokenValidationResponse;
import by.algin.userservice.constants.MessageConstants;
import by.algin.userservice.entity.User;
import by.algin.userservice.exception.InvalidTokenException;
import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.exception.UserServiceErrorCode;
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
        log.info(MessageConstants.PROCESSING_LOGIN_REQUEST, loginRequest.getUsernameOrEmail());

        try {
            User user = authenticateAndGetUser(loginRequest);

            validateUserAccount(user);
            logUserRoles(user);

            AuthResponse authResponse = createAuthResponse(user);

            log.info(MessageConstants.LOGIN_SUCCESSFUL_FOR_USER, user.getUsername());
            return ApiResponse.success(MessageConstants.LOGIN_SUCCESSFUL, authResponse);

        } catch (BadCredentialsException e) {
            log.error(MessageConstants.INVALID_CREDENTIALS_FOR_USER, loginRequest.getUsernameOrEmail());
            throw e;
        } catch (DisabledException e) {
            log.error(MessageConstants.ACCOUNT_DISABLED_FOR_USER, loginRequest.getUsernameOrEmail());
            throw e;
        }
    }

    public ApiResponse<AuthResponse> refreshToken(TokenRefreshRequest refreshRequest) {
        log.info(MessageConstants.PROCESSING_TOKEN_REFRESH);

        String refreshToken = refreshRequest.getRefreshToken();
        if (!StringUtils.hasText(refreshToken)) {
            log.error(MessageConstants.REFRESH_TOKEN_NULL_OR_EMPTY_LOG);
            throw new InvalidTokenException(MessageConstants.REFRESH_TOKEN_NULL_OR_EMPTY);
        }

        validateRefreshToken(refreshToken);

        String username = jwtService.getUsernameFromToken(refreshToken);
        User user = findUserByUsername(username);

        validateUserAccount(user);

        AuthResponse authResponse = createRefreshAuthResponse(user, refreshToken);

        log.info(MessageConstants.TOKEN_REFRESHED_FOR_USER, user.getUsername());
        return ApiResponse.success(MessageConstants.TOKEN_REFRESHED_SUCCESSFULLY, authResponse);
    }

    public ApiResponse<TokenValidationResponse> validateToken(TokenValidationRequest validationRequest) {
        log.info(MessageConstants.PROCESSING_TOKEN_VALIDATION);

        try {
            Claims claims = jwtService.getAllClaimsFromToken(validationRequest.getToken());
            String username = jwtService.getUsernameFromClaims(claims);

            if (jwtService.isTokenExpired(claims)) {
                log.warn(MessageConstants.INVALID_TOKEN_PROVIDED);
                return createInvalidTokenResponse(MessageConstants.INVALID_TOKEN);
            }

            User user = findUserByUsername(username);

            if (!user.isEnabled()) {
                log.warn(MessageConstants.TOKEN_VALID_USER_DISABLED, username);
                return createInvalidTokenResponse(MessageConstants.USER_IS_DISABLED);
            }

            TokenValidationResponse validationResponse = authMapper.toTokenValidationResponse(user, claims, true);
            log.info(MessageConstants.TOKEN_VALIDATED_FOR_USER, username);
            return ApiResponse.success(MessageConstants.TOKEN_IS_VALID, validationResponse);
        } catch (Exception e) {
            log.warn(MessageConstants.INVALID_TOKEN_PROVIDED);
            return createInvalidTokenResponse(MessageConstants.INVALID_TOKEN);
        }
    }

    private User authenticateAndGetUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

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
            throw new DisabledException(MessageConstants.ERROR_MSG_ACCOUNT_DISABLED_FOR_USER + user.getUsername());
        }
    }

    private void validateRefreshToken(String refreshToken) {
        try {
            Claims claims = jwtService.getAllClaimsFromToken(refreshToken);

            if (jwtService.isTokenExpired(claims)) {
                log.error(MessageConstants.INVALID_REFRESH_TOKEN_FORMAT_LOG);
                throw new InvalidTokenException(MessageConstants.INVALID_REFRESH_TOKEN_FORMAT);
            }

            if (!jwtService.isRefreshToken(claims)) {
                log.error(MessageConstants.TOKEN_NOT_REFRESH_TOKEN_LOG);
                throw new InvalidTokenException(MessageConstants.TOKEN_NOT_REFRESH_TOKEN);
            }
        } catch (InvalidTokenException e) {
            throw e; 
        } catch (Exception e) {
            log.error(MessageConstants.INVALID_REFRESH_TOKEN_FORMAT_LOG);
            throw new InvalidTokenException(MessageConstants.INVALID_REFRESH_TOKEN_FORMAT);
        }
    }

    private void logUserRoles(User user) {
        log.info(MessageConstants.USER_FOUND, user.getUsername());
        log.info(MessageConstants.USER_ROLES_BEFORE_MAPPING, user.getRoles());
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> log.info(MessageConstants.ROLE_LOG, role.getName()));
        }
    }

    private AuthResponse createAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Claims accessTokenClaims = jwtService.getAllClaimsFromToken(accessToken);
        Long expiresIn = accessTokenClaims.getExpiration().getTime() / 1000;

        AuthResponse authResponse = authMapper.toAuthResponse(user, accessToken, refreshToken, expiresIn);
        log.info(MessageConstants.AUTH_RESPONSE_ROLES, authResponse.getRoles());

        return authResponse;
    }

    private AuthResponse createRefreshAuthResponse(User user, String refreshToken) {
        String accessToken = jwtService.generateAccessToken(user);

        Claims accessTokenClaims = jwtService.getAllClaimsFromToken(accessToken);
        Long expiresIn = accessTokenClaims.getExpiration().getTime() / 1000;

        return authMapper.toAuthResponse(user, accessToken, refreshToken, expiresIn);
    }

    private ApiResponse<TokenValidationResponse> createInvalidTokenResponse(String message) {
        TokenValidationResponse invalidResponse = authMapper.toInvalidTokenResponse(message);
        return ApiResponse.error(UserServiceErrorCode.INVALID_TOKEN.getCode(), message, invalidResponse);
    }
}