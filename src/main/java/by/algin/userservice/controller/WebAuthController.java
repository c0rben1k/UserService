package by.algin.userservice.controller;

import by.algin.constants.CommonPathConstants;
import by.algin.userservice.constants.MessageConstants;
import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final UserService userService;

    @GetMapping(PathConstants.AUTH_LOGIN)
    public String showLoginForm() {
        return PathConstants.TEMPLATE_LOGIN;
    }

    @GetMapping(PathConstants.AUTH_REGISTER)
    public String showRegisterForm() {
        return PathConstants.TEMPLATE_REGISTER;
    }

    @GetMapping(PathConstants.AUTH_REGISTRATION_SUCCESS)
    public String showRegistrationSuccess() {
        return PathConstants.TEMPLATE_REGISTRATION_SUCCESS;
    }

    @GetMapping(PathConstants.AUTH_CONFIRM)
    public String confirmAccount(@RequestParam(CommonPathConstants.PARAM_TOKEN) String token, Model model) {
        try {
            log.info("Processing web account confirmation with token");
            userService.confirmAccount(token);
            model.addAttribute("message", MessageConstants.ACCOUNT_CONFIRMED_SUCCESSFULLY);
            return PathConstants.TEMPLATE_ACCOUNT_CONFIRMED;
        } catch (Exception e) {
            log.error("Account confirmation failed: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return PathConstants.TEMPLATE_TOKEN_EXPIRED;
        }
    }

    @GetMapping(PathConstants.AUTH_TOKEN_EXPIRED)
    public String showTokenExpired() {
        return PathConstants.TEMPLATE_TOKEN_EXPIRED;
    }

    @GetMapping(PathConstants.AUTH_RESEND_CONFIRMATION)
    public String showResendConfirmation() {
        return PathConstants.TEMPLATE_TOKEN_RESENT;
    }

    @GetMapping(PathConstants.ROOT)
    public String showIndex() {
        return PathConstants.TEMPLATE_INDEX;
    }

    @GetMapping(PathConstants.DASHBOARD)
    public String showDashboard() {
        return PathConstants.TEMPLATE_DASHBOARD;
    }

    @GetMapping(PathConstants.ADMIN_DASHBOARD)
    public String showAdminDashboard() {
        return PathConstants.TEMPLATE_ADMIN_DASHBOARD;
    }
}
