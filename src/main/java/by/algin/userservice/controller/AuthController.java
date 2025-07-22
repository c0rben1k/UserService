package by.algin.userservice.controller;

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
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final TokenValidationService tokenValidationService;

    @PostMapping(PathConstants.REGISTER_ENDPOINT)
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Processing registration request for: {}", registerRequest.getUsername());
        ApiResponse<UserResponse> response = userService.registerUser(registerRequest);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping(PathConstants.LOGIN_ENDPOINT)
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Processing login request for: {}", loginRequest.getUsernameOrEmail());
        ApiResponse<AuthResponse> response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(PathConstants.REFRESH_TOKEN)
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("Processing token refresh request");
        ApiResponse<AuthResponse> response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(PathConstants.API_AUTH_VALIDATE_TOKEN)
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken(
            @Valid @RequestBody TokenValidationRequest request) {
        log.info("Processing token validation request");
        ApiResponse<TokenValidationResponse> response = authService.validateToken(request);
        return ResponseEntity.ok(response);
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

    @PostMapping(PathConstants.CONFIRM_ENDPOINT)
    public ResponseEntity<ApiResponse<String>> confirmAccount(@Valid @RequestBody TokenValidationRequest request) {
        log.debug("Processing account confirmation request");
        ApiResponse<String> response = userService.confirmAccount(request.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping(PathConstants.RESEND_CONFIRMATION_ENDPOINT)
    public ResponseEntity<ApiResponse<String>> resendConfirmation(@RequestParam(PathConstants.PARAM_EMAIL) String email) {
        log.info("Processing resend confirmation request for: {}", email);
        userService.resendConfirmationToken(email);
        ApiResponse<String> response = ApiResponse.success(MessageConstants.CONFIRMATION_EMAIL_RESENT, null);
        return ResponseEntity.ok(response);и
    }

    @PostMapping(PathConstants.EMAIL_BY_TOKEN_ENDPOINT)
    public ResponseEntity<ApiResponse<String>> getEmailByToken(@Valid @RequestBody TokenValidationRequest request) {
        log.debug("Processing email retrieval request");
        String email = userService.getUserEmailByToken(request.getToken());
        ApiResponse<String> response = ApiResponse.success("Email retrieved successfully", email);
        return ResponseEntity.ok(response);
    }
}