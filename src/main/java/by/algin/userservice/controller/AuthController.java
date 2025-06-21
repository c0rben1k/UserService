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

    @PostMapping(PathConstants.REGISTER)
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Processing registration request for: {}", registerRequest.getUsername());
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping(PathConstants.LOGIN)
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Processing login request for: {}", loginRequest.getUsernameOrEmail());
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(PathConstants.REFRESH_TOKEN)
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("Processing token refresh request");
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping(PathConstants.VALIDATE_TOKEN)
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken(
            @Valid @RequestBody TokenValidationRequest request) {
        log.info("Processing token validation request");
        return ResponseEntity.ok(authService.validateToken(request));
    }

    @GetMapping(PathConstants.CONFIRM)
    public ResponseEntity<ApiResponse<String>> confirmAccount(@RequestParam("token") String token) {
        log.info("Processing account confirmation with token");
        return ResponseEntity.ok(userService.confirmAccount(token));
    }

    @PostMapping(PathConstants.RESEND_CONFIRMATION)
    public ResponseEntity<ApiResponse<String>> resendConfirmation(@RequestParam("email") String email) {
        log.info("Processing resend confirmation request for: {}", email);
        userService.resendConfirmationToken(email);
        return ResponseEntity.ok(ApiResponse.success(MessageConstants.CONFIRMATION_EMAIL_RESENT, null));
    }
}