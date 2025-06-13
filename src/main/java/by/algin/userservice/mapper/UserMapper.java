package by.algin.userservice.mapper;

import by.algin.dto.request.RegisterRequest;
import by.algin.dto.response.UserResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles() != null ?
                        user.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toCollection(HashSet::new)) :
                        new HashSet<>())
                .build();
    }

    public User toUserEntity(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return null;
        }

        return User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .build();
    }
}