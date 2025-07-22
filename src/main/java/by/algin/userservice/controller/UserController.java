package by.algin.userservice.controller;


import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.UserResponse;
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

    @GetMapping(PathConstants.SEARCH)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByField(@RequestParam(PathConstants.PARAM_FIELD) String field,
                                                                   @RequestParam(PathConstants.PARAM_VALUE) String value) {
        log.info("Searching user by field: {} with value: {}", field, value);
        ApiResponse<UserResponse> response = userService.getUserByField(field, value);
        return ResponseEntity.ok(response);
    }

    @GetMapping(PathConstants.USER_BY_ID)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        log.info("Getting user by ID: {}", userId);
        ApiResponse<UserResponse> response = userService.getUserByField("id", userId.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping(PathConstants.USER_EXISTS)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Boolean>> userExists(@PathVariable Long userId) {
        log.info("Checking if user exists with ID: {}", userId);
        ApiResponse<Boolean> response = userService.checkUserExists(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(PathConstants.BATCH_USERS)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<java.util.List<UserResponse>>> getUsersByIds(@RequestParam("ids") java.util.List<Long> userIds) {
        log.info("Getting users by IDs: {}", userIds);
        ApiResponse<java.util.List<UserResponse>> response = userService.getUsersByIdsWithValidation(userIds);
        return ResponseEntity.ok(response);
    }

}