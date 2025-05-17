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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ConfirmationService confirmationService;

    @Transactional
    public ApiResponse<UserResponse> registerUser(RegisterRequest registerRequest) {
        log.info("Registering new user with username: {}", registerRequest.getUsername());

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(RoleNotFoundException::new);

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .enabled(false)
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .confirmationToken(tokenService.generateToken())
                .tokenCreationTime(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        confirmationService.sendConfirmationEmail(savedUser);

        return new ApiResponse<>(
                true,
                "User registered successfully. Please check your email for confirmation instructions.",
                mapToUserResponse(savedUser)
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

        return mapToUserResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .build();
    }
}