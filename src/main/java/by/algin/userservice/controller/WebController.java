package by.algin.userservice.controller;

import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.constants.RoleConstants;
import by.algin.userservice.dto.response.UserResponse;
import by.algin.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserService userService;

    @GetMapping(PathConstants.ROOT)
    public String home() {
        return PathConstants.TEMPLATE_INDEX;
    }

    @GetMapping(PathConstants.DASHBOARD)
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            try {
                UserResponse user = userService.getUserByField("username", authentication.getName());
                model.addAttribute("user", user);
                log.info("User {} accessed dashboard page", user.getUsername());
            } catch (Exception e) {
                log.error("Error getting user info for dashboard page", e);
                model.addAttribute("user", null);
            }
        } else {
            model.addAttribute("user", null);
        }
        return PathConstants.TEMPLATE_DASHBOARD;
    }

    @GetMapping(PathConstants.ADMIN_DASHBOARD)
    @PreAuthorize("hasAuthority('" + RoleConstants.ADMIN + "')")
    public String adminDashboard() {
        return PathConstants.TEMPLATE_ADMIN_DASHBOARD;
    }
}