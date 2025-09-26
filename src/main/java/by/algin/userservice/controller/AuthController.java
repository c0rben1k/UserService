package by.algin.userservice.controller;

import by.algin.api.AuthApi;
import by.algin.constants.CommonPathConstants;
import by.algin.dto.request.LoginRequest;
import by.algin.dto.request.RegisterRequest;
import by.algin.dto.request.TokenRefreshRequest;
import by.algin.dto.request.TokenValidationRequest;
import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.AuthResponse;
import by.algin.dto.response.TokenValidationResponse;
import by.algin.dto.response.UserResponse;

import by.algin.userservice.constants.MessageConstants;
import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.service.AuthService;
import by.algin.userservice.service.TokenValidationService;
import by.algin.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(PathConstants.API_AUTH_BASE)
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final UserService userService;
    private final TokenValidationService tokenValidationService;

    @Override
    @PostMapping(PathConstants.REGISTER_ENDPOINT)
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        log.info("Processing registration request for: {}", registerRequest.getUsername());
        return userService.registerUser(registerRequest);
    }

    @Override
    @PostMapping(PathConstants.LOGIN_ENDPOINT)
    public ApiResponse<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Processing login request for: {}", loginRequest.getUsernameOrEmail());
        return authService.login(loginRequest);
    }

    @Override
    @PostMapping(PathConstants.REFRESH_TOKEN)
    public ApiResponse<AuthResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        log.info("Processing token refresh request");
        TokenRefreshRequest request = TokenRefreshRequest.builder().refreshToken(refreshToken).build();
        return authService.refreshToken(request);
    }

    @Override
    @PostMapping(PathConstants.API_AUTH_VALIDATE_TOKEN)
    public ApiResponse<Boolean> validateToken(@RequestBody TokenValidationRequest tokenValidationRequest) {
        log.info("Processing token validation request");
        ApiResponse<TokenValidationResponse> validationResponse = authService.validateToken(tokenValidationRequest);
        if (validationResponse.isSuccess() && validationResponse.getData() != null) {
            return ApiResponse.success(validationResponse.getData().isValid());
        } else {
            return ApiResponse.success(false);
        }
    }

    @GetMapping(PathConstants.VALIDATE)
    public ResponseEntity<Boolean> validateTokenSimple(@RequestHeader("Authorization") String authHeader) {
        boolean isValid = tokenValidationService.validateTokenSimple(authHeader);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping(PathConstants.VALIDATE + "/detailed")
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateTokenDetailed(@RequestHeader("Authorization") String authHeader) {
        ApiResponse<TokenValidationResponse> response = tokenValidationService.validateTokenFromHeader(authHeader);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @Override
    @PostMapping(PathConstants.CONFIRM_ENDPOINT)
    public ApiResponse<String> confirmAccount(@RequestParam(CommonPathConstants.PARAM_TOKEN) String token) {
        log.debug("Processing account confirmation request");
        return userService.confirmAccount(token);
    }

    @Override
    @PostMapping(PathConstants.RESEND_CONFIRMATION_ENDPOINT)
    public ApiResponse<String> resendConfirmation(@RequestParam(CommonPathConstants.PARAM_EMAIL) String email) {
        log.info("Processing resend confirmation request for: {}", email);
        userService.resendConfirmationToken(email);
        return ApiResponse.success(MessageConstants.CONFIRMATION_EMAIL_RESENT, null);
    }

    @Override
    @GetMapping(PathConstants.EMAIL_BY_TOKEN_ENDPOINT)
    public ApiResponse<String> getEmailByToken(@RequestParam(CommonPathConstants.PARAM_TOKEN) String token) {
        log.debug("Processing email retrieval request");
        String email = userService.getUserEmailByToken(token);
        return ApiResponse.success("Email retrieved successfully", email);
    }
}