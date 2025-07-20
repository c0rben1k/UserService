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
import org.springframework.web.bind.annotation.PostMapping;
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

            try {
                String userEmail = userService.getUserEmailByToken(token);
                if (userEmail != null) {
                    model.addAttribute("userEmail", userEmail);
                    model.addAttribute("expiredToken", token);
                    log.info("Found user email for expired token: {}", userEmail);
                } else {
                    model.addAttribute("expiredToken", token);
                }
            } catch (Exception emailEx) {
                log.warn("Could not retrieve user email for token: {}", emailEx.getMessage());
                model.addAttribute("expiredToken", token);
            }

            return PathConstants.TEMPLATE_TOKEN_EXPIRED;
        }
    }

    @GetMapping(PathConstants.AUTH_TOKEN_EXPIRED)
    public String showTokenExpired(@RequestParam(value = CommonPathConstants.PARAM_EMAIL, required = false) String email,
                                  @RequestParam(value = CommonPathConstants.PARAM_TOKEN, required = false) String token,
                                  Model model) {
        if (email != null && !email.isEmpty()) {
            model.addAttribute("userEmail", email);
            log.info("Showing token expired page with email: {}", email);
        }
        if (token != null && !token.isEmpty()) {
            model.addAttribute("expiredToken", token);
            log.info("Showing token expired page with expired token");
        }
        return PathConstants.TEMPLATE_TOKEN_EXPIRED;
    }

    @GetMapping(PathConstants.AUTH_RESEND_CONFIRMATION)
    public String showResendConfirmation(@RequestParam(value = CommonPathConstants.PARAM_EMAIL, required = false) String email, Model model) {
        if (email != null && !email.isEmpty()) {
            model.addAttribute("userEmail", email);
            log.info("Showing resend confirmation page for email: {}", email);
        }
        return PathConstants.TEMPLATE_TOKEN_RESENT;
    }

    @PostMapping(PathConstants.AUTH_RESEND_CONFIRMATION)
    public String processResendConfirmation(@RequestParam(CommonPathConstants.PARAM_EMAIL) String email, Model model) {
        try {
            log.info("Processing web resend confirmation request for: {}", email);
            userService.resendConfirmationToken(email);
            model.addAttribute("message", MessageConstants.CONFIRMATION_EMAIL_RESENT);
            model.addAttribute("email", email);
            return PathConstants.TEMPLATE_TOKEN_RESENT;
        } catch (Exception e) {
            log.error("Resend confirmation failed: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("userEmail", email);
            return PathConstants.TEMPLATE_TOKEN_EXPIRED;
        }
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
