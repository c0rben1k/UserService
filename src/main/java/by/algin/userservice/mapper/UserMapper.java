package by.algin.userservice.mapper;

import by.algin.userservice.DTO.request.RegisterRequest;
import by.algin.userservice.DTO.response.UserResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User toUserEntity(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return null;
        }

        return User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .enabled(false)
                .build();
    }

}