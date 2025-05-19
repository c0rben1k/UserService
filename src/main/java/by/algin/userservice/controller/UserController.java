package by.algin.userservice.controller;

import by.algin.userservice.DTO.response.UserResponse;
import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(PathConstants.API_USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("Getting user with id: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/by-username/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        log.info("Getting user with username: {}", username);
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/by-email/{email}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("Getting user with email: {}", email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}