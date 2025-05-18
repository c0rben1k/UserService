package by.algin.userservice.security;

import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.constants.RoleConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectPath = isAdmin(authentication) ? PathConstants.ADMIN_DASHBOARD : PathConstants.DASHBOARD;
        response.sendRedirect(redirectPath);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> RoleConstants.ROLE_ADMIN.equals(auth.getAuthority()));
    }
}