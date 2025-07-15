package by.algin.userservice.controller;

import by.algin.constants.CommonPathConstants;
import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.UserResponse;
import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResponse<UserResponse> getUserByField(@RequestParam(CommonPathConstants.PARAM_FIELD) String field,
                                                   @RequestParam(CommonPathConstants.PARAM_VALUE) String value) {
        log.info("Searching user by field: {} with value: {}", field, value);
        return userService.getUserByField(field, value);
    }

    @GetMapping(PathConstants.USER_BY_ID)
    @PreAuthorize("isAuthenticated()")
    public UserResponse getUserById(@PathVariable Long userId) {
        log.info("Getting user by ID: {}", userId);
        ApiResponse<UserResponse> response = userService.getUserByField("id", userId.toString());
        return response.getData();
    }

    @GetMapping(PathConstants.USER_EXISTS)
    @PreAuthorize("isAuthenticated()")
    public Boolean userExists(@PathVariable Long userId) {
        log.info("Checking if user exists with ID: {}", userId);
        try {
            userService.getUserByField("id", userId.toString());
            return true;
        } catch (Exception e) {
            log.debug("User with ID {} does not exist: {}", userId, e.getMessage());
            return false;
        }
    }

    @GetMapping(PathConstants.BATCH_USERS)
    @PreAuthorize("isAuthenticated()")
    public java.util.List<UserResponse> getUsersByIds(@RequestParam("ids") java.util.List<Long> userIds) {
        log.info("Getting users by IDs: {}", userIds);
        return userService.getUsersByIds(userIds);
    }

}