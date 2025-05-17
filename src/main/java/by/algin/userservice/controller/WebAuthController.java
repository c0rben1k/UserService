package by.algin.userservice.controller;

import by.algin.userservice.DTO.request.RegisterRequest;
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
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterRequest user,
                           BindingResult result,
                           Model model) {
        try {
            if (result.hasErrors()) {
                return "register";
            }

            userService.registerUser(user);
            return "redirect:/auth/registration-success";
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("emailError", "Username already exists");
            return "register";
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("emailError", "Email already exists");
            return "register";
        } catch (PasswordsDoNotMatchException e) {
            model.addAttribute("passwordError", "Passwords don't match");
            return "register";
        }
    }

    @GetMapping("/registration-success")
    public String registrationSuccess() {
        return "registration-success";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/confirm")
    public String confirmAccount(@RequestParam("token") String token, Model model) {
        try {
            userService.confirmAccount(token);
            return "account-confirmed";
        } catch (TokenExpiredException e) {
            model.addAttribute("email", e.getEmail());
            return "token-expired";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/token-expired")
    public String tokenExpired(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "token-expired";
    }

    @PostMapping("/resend-confirmation")
    public String resendToken(@RequestParam("email") String email, Model model) {
        try {
            userService.resendConfirmationToken(email);
            return "token-resent";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}