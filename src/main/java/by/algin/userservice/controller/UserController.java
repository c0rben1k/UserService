package by.algin.userservice.controller;


import by.algin.api.UserApi;
import by.algin.constants.CommonPaginationConstants;
import by.algin.constants.CommonPathConstants;
import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.PagedResponse;
import by.algin.dto.response.UserResponse;
import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(PathConstants.API_USERS)
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @GetMapping(CommonPathConstants.PATH_VAR_USER_ID)
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserResponse> getUserById(@PathVariable("userId") Long userId) {
        log.info("Getting user by ID: {}", userId);
        return userService.getUserByField("id", userId.toString());
    }

    @Override
    @GetMapping(PathConstants.USER_EXISTS)
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Boolean> userExists(@PathVariable("userId") Long userId) {
        log.info("Checking if user exists with ID: {}", userId);
        return userService.checkUserExists(userId);
    }

    @Override
    @GetMapping(PathConstants.SEARCH)
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<PagedResponse<UserResponse>> searchUsers(
            @RequestParam(CommonPathConstants.PARAM_FIELD) String field,
            @RequestParam(CommonPathConstants.PARAM_VALUE) String value,
            @RequestParam(value = "page", defaultValue = "" + CommonPaginationConstants.DEFAULT_PAGE) int page,
            @RequestParam(value = "size", defaultValue = "" + CommonPaginationConstants.DEFAULT_PAGE_SIZE) int size) {
        log.info("Searching users by field: {} with value: {}, page: {}, size: {}", field, value, page, size);

        ApiResponse<UserResponse> singleUserResponse = userService.getUserByField(field, value);
        List<UserResponse> content = singleUserResponse.isSuccess() && singleUserResponse.getData() != null
                ? List.of(singleUserResponse.getData())
                : List.of();

        PagedResponse<UserResponse> pagedResponse = PagedResponse.<UserResponse>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements((long) content.size())
                .totalPages(content.isEmpty() ? 0 : 1)
                .first(true)
                .last(true)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        return ApiResponse.success(pagedResponse);
    }

    @Override
    @GetMapping(PathConstants.BATCH_USERS)
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<UserResponse>> getUsersByIds(@RequestParam("userIds") List<Long> userIds) {
        log.info("Getting users by IDs: {}", userIds);
        return userService.getUsersByIdsWithValidation(userIds);
    }

}