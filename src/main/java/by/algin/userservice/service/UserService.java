package by.algin.userservice.service;

import by.algin.constants.CommonServiceConstants;
import by.algin.dto.request.RegisterRequest;
import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.UserResponse;
import by.algin.userservice.constants.MessageConstants;
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
        log.info(MessageConstants.REGISTERING_USER, registerRequest.getUsername());
        userValidator.validateRegistrationRequest(registerRequest);
        Role userRole = roleRepository.findByName(RoleConstants.USER)
                .orElseThrow(RoleNotFoundException::new);
        User user = userMapper.toUserEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        user.setConfirmationToken(tokenService.generateToken());
        user.setTokenCreationTime(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.info(MessageConstants.USER_REGISTERED_WITH_ID, savedUser.getId());
        confirmationService.sendConfirmationEmail(savedUser);
        UserResponse userResponse = userMapper.toUserResponse(savedUser);
        return ApiResponse.success(MessageConstants.USER_REGISTERED_SUCCESSFULLY, userResponse);
    }

    @Transactional
    public ApiResponse<String> confirmAccount(String token) {
        confirmationService.confirmAccount(token);
        return ApiResponse.success(MessageConstants.ACCOUNT_CONFIRMED_SUCCESSFULLY, null);
    }

    @Transactional
    public void resendConfirmationToken(String email) {
        log.info(MessageConstants.CHECKING_RATE_LIMIT, email);
        rateLimiter.checkRateLimit(email);
        confirmationService.resendConfirmationToken(email);
    }

    public ApiResponse<UserResponse> getUserByField(String field, String value) {
        log.info(MessageConstants.GETTING_USER_BY_FIELD, field, value);

        User user;
        switch (field.toLowerCase()) {
            case CommonServiceConstants.SEARCH_FIELD_ID:
                try {
                    Long id = Long.parseLong(value);
                    user = userRepository.findById(id)
                            .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND_WITH_ID + value));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(MessageConstants.INVALID_ID_FORMAT + value);
                }
                break;
            case CommonServiceConstants.SEARCH_FIELD_USERNAME:
                user = userRepository.findByUsername(value)
                        .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND_WITH_USERNAME + value));
                break;
            case CommonServiceConstants.SEARCH_FIELD_EMAIL:
                user = userRepository.findByEmail(value)
                        .orElseThrow(() -> new UserNotFoundException(MessageConstants.USER_NOT_FOUND_WITH_EMAIL + value));
                break;
            default:
                throw new IllegalArgumentException(MessageConstants.INVALID_SEARCH_FIELD + field + MessageConstants.VALID_SEARCH_FIELDS);
        }

        return ApiResponse.success(MessageConstants.USER_FOUND_SIMPLE, userMapper.toUserResponse(user));
    }

}