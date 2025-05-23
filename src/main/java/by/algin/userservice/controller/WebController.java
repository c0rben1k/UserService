package by.algin.userservice.controller;

import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.constants.RoleConstants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(PathConstants.ROOT)
    public String home() {
        return PathConstants.TEMPLATE_INDEX;
    }

    @GetMapping(PathConstants.DASHBOARD)
    public String dashboard(Authentication authentication, Model model) {
        return PathConstants.TEMPLATE_DASHBOARD;
    }

    @GetMapping(PathConstants.ADMIN_DASHBOARD)
    @PreAuthorize("hasAuthority('" + RoleConstants.ROLE_ADMIN + "')")
    public String adminDashboard() {
        return PathConstants.TEMPLATE_ADMIN_DASHBOARD;
    }
}