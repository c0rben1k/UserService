package by.algin.userservice.service;

import by.algin.userservice.dto.request.RegisterRequest;
import by.algin.userservice.dto.response.ApiResponse;
import by.algin.userservice.dto.response.UserResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.exception.RoleNotFoundException;
import by.algin.userservice.mapper.UserMapper;
import by.algin.userservice.constants.RoleConstants;
import by.algin.userservice.repository.RoleRepository;
import by.algin.userservice.repository.UserRepository;
import by.algin.userservice.util.RateLimiter;
import by.algin.userservice.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ConfirmationService confirmationService;
    private final UserMapper userMapper;
    private final RateLimiter rateLimiter;
    private final UserValidator userValidator;

    @Transactional
    public ApiResponse<UserResponse> registerUser(RegisterRequest registerRequest) {
        log.info("Registering new user with username: {}", registerRequest.getUsername());
        userValidator.validateRegistrationRequest(registerRequest);
        Role userRole = roleRepository.findByName(RoleConstants.USER)
                .orElseThrow(RoleNotFoundException::new);
        User user = userMapper.toUserEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        user.setConfirmationToken(tokenService.generateToken());
        user.setTokenCreationTime(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());
        confirmationService.sendConfirmationEmail(savedUser);
        UserResponse userResponse = userMapper.toUserResponse(savedUser);
        return new ApiResponse<>(
                true,
                "User registered successfully. Please check your email for confirmation instructions.",
                userResponse
        );
    }

    @Transactional
    public ApiResponse<String> confirmAccount(String token) {
        confirmationService.confirmAccount(token);
        return new ApiResponse<>(true, "Account confirmed successfully. You can now login.", null);
    }

    @Transactional
    public void resendConfirmationToken(String email) {
        log.info("Checking rate limit for resend confirmation token request for email: {}", email);
        rateLimiter.checkRateLimit(email);
        confirmationService.resendConfirmationToken(email);
    }

    public UserResponse getUserByField(String field, String value) {
        log.info("Getting user by field: {} with value: {}", field, value);

        User user;
        switch (field.toLowerCase()) {
            case "id":
                try {
                    Long id = Long.parseLong(value);
                    user = userRepository.findById(id)
                            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + value));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid id format: " + value);
                }
                break;
            case "username":
                user = userRepository.findByUsername(value)
                        .orElseThrow(() -> new UserNotFoundException("User not found with username: " + value));
                break;
            case "email":
                user = userRepository.findByEmail(value)
                        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + value));
                break;
            default:
                throw new IllegalArgumentException("Invalid search field: " + field + ". Use 'id', 'username', or 'email'.");
        }

        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> searchUsers(String field, String value) {
        log.info("Searching users by field: {} with value: {}", field, value);
        try {
            UserResponse user = getUserByField(field, value);
            return Collections.singletonList(user);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}