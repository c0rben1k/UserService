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
}