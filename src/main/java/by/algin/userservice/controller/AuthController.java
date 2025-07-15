package by.algin.userservice.controller;

import by.algin.dto.request.LoginRequest;
import by.algin.dto.request.RegisterRequest;
import by.algin.dto.request.TokenRefreshRequest;
import by.algin.dto.request.TokenValidationRequest;
import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.AuthResponse;
import by.algin.dto.response.TokenValidationResponse;
import by.algin.dto.response.UserResponse;
import by.algin.constants.CommonPathConstants;
import by.algin.userservice.constants.MessageConstants;
import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.service.AuthService;
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

    @PostMapping(CommonPathConstants.REGISTER_ENDPOINT)
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Processing registration request for: {}", registerRequest.getUsername());
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping(CommonPathConstants.LOGIN_ENDPOINT)
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Processing login request for: {}", loginRequest.getUsernameOrEmail());
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(PathConstants.REFRESH_TOKEN)
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("Processing token refresh request");
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping(CommonPathConstants.API_AUTH_VALIDATE_TOKEN)
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken(
            @Valid @RequestBody TokenValidationRequest request) {
        log.info("Processing token validation request");
        return ResponseEntity.ok(authService.validateToken(request));
    }

    @GetMapping(PathConstants.VALIDATE)
    public ResponseEntity<Boolean> validateTokenSimple(@RequestParam("token") String token) {
        log.info("=== SIMPLE TOKEN VALIDATION START ===");
        log.info("Token received: {}", token.substring(0, Math.min(50, token.length())) + "...");
        try {
            TokenValidationRequest request = new TokenValidationRequest();
            request.setToken(token);
            log.info("Calling authService.validateToken()");
            ApiResponse<TokenValidationResponse> response = authService.validateToken(request);
            log.info("AuthService response: success={}, data={}", response.isSuccess(), response.getData());
            boolean result = response.isSuccess() && response.getData() != null && response.getData().isValid();
            log.info("Final validation result: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage(), e);
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping(CommonPathConstants.CONFIRM_ENDPOINT)
    public ResponseEntity<ApiResponse<String>> confirmAccount(@RequestParam(CommonPathConstants.PARAM_TOKEN) String token) {
        log.info("Processing account confirmation with token");
        return ResponseEntity.ok(userService.confirmAccount(token));
    }

    @PostMapping(CommonPathConstants.RESEND_CONFIRMATION_ENDPOINT)
    public ResponseEntity<ApiResponse<String>> resendConfirmation(@RequestParam(CommonPathConstants.PARAM_EMAIL) String email) {
        log.info("Processing resend confirmation request for: {}", email);
        userService.resendConfirmationToken(email);
        return ResponseEntity.ok(ApiResponse.success(MessageConstants.CONFIRMATION_EMAIL_RESENT, null));
    }
}