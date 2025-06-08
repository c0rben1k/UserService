package by.algin.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean isValid;
    private Long userId;
    private String username;
    private String email;
    private HashSet<String> roles;
    private HashMap<String, Object> claims;
}