package by.algin.userservice.controller;

import by.algin.userservice.DTO.request.RegisterRequest;
import by.algin.userservice.constants.PathConstants;
import by.algin.userservice.exception.EmailAlreadyExistsException;
import by.algin.userservice.exception.PasswordsDoNotMatchException;
import by.algin.userservice.exception.TokenExpiredException;
import by.algin.userservice.exception.UsernameAlreadyExistsException;
import by.algin.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class WebAuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegisterRequest());
        return PathConstants.TEMPLATE_REGISTER;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterRequest user,
                           BindingResult result,
                           Model model) {
        try {
            if (result.hasErrors()) {
                return PathConstants.TEMPLATE_REGISTER;
            }

            userService.registerUser(user);
            return "redirect:/auth/registration-success";
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("emailError", "Username already exists");
            return PathConstants.TEMPLATE_REGISTER;
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("emailError", "Email already exists");
            return PathConstants.TEMPLATE_REGISTER;
        } catch (PasswordsDoNotMatchException e) {
            model.addAttribute("passwordError", "Passwords don't match");
            return PathConstants.TEMPLATE_REGISTER;
        }
    }

    @GetMapping("/registration-success")
    public String registrationSuccess() {
        return PathConstants.TEMPLATE_REGISTRATION_SUCCESS;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return PathConstants.TEMPLATE_LOGIN;
    }

    @GetMapping("/confirm")
    public String confirmAccount(@RequestParam("token") String token, Model model) {
        try {
            userService.confirmAccount(token);
            return PathConstants.TEMPLATE_ACCOUNT_CONFIRMED;
        } catch (TokenExpiredException e) {
            model.addAttribute("email", e.getEmail());
            return PathConstants.TEMPLATE_TOKEN_EXPIRED;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return PathConstants.TEMPLATE_ERROR;
        }
    }

    @GetMapping("/token-expired")
    public String tokenExpired(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return PathConstants.TEMPLATE_TOKEN_EXPIRED;
    }

    @PostMapping("/resend-confirmation")
    public String resendToken(@RequestParam("email") String email, Model model) {
        try {
            userService.resendConfirmationToken(email);
            return PathConstants.TEMPLATE_TOKEN_RESENT;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return PathConstants.TEMPLATE_ERROR;
        }
    }
}