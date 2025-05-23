package by.algin.userservice.service;

import by.algin.userservice.DTO.request.RegisterRequest;
import by.algin.userservice.DTO.response.ApiResponse;
import by.algin.userservice.DTO.response.UserResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import by.algin.userservice.exception.EmailAlreadyExistsException;
import by.algin.userservice.exception.PasswordsDoNotMatchException;
import by.algin.userservice.exception.UsernameAlreadyExistsException;
import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.exception.RoleNotFoundException;
import by.algin.userservice.mapper.UserMapper;
import by.algin.userservice.constants.RoleConstants;
import by.algin.userservice.repository.RoleRepository;
import by.algin.userservice.repository.UserRepository;
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

    @Transactional
    public ApiResponse<UserResponse> registerUser(RegisterRequest registerRequest) {
        log.info("Registering new user with username: {}", registerRequest.getUsername());
        validateRegistrationRequest(registerRequest);
        Role userRole = roleRepository.findByName(RoleConstants.ROLE_USER)
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
        confirmationService.resendConfirmationToken(email);
    }

    public UserResponse getUserById(Long id) {
        log.info("Getting user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toUserResponse(user);
    }

    private void validateRegistrationRequest(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
    }
}