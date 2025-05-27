package by.algin.userservice.mapper;

import by.algin.userservice.dto.request.RegisterRequest;
import by.algin.userservice.dto.response.UserResponse;
import by.algin.userservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
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