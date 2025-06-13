package by.algin.userservice.controller;

import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.UserResponse;
import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(PathConstants.API_USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(PathConstants.API_USERS_SEARCH)
    public ApiResponse<UserResponse> getUserByField(@RequestParam String field, @RequestParam String value) {
        return userService.getUserByField(field, value);
    }
}