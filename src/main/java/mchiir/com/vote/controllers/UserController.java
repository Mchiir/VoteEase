package mchiir.com.vote.controllers;

import io.github.cdimascio.dotenv.Dotenv;
import mchiir.com.vote.dtos.UserDTO;
import mchiir.com.vote.models.User;
import mchiir.com.vote.models.enums.Role;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private final UserService userService;
    private final Dotenv dotenv;

    public UserController(UserService userService) {
        this.userService =  userService;
        dotenv = Dotenv.load();
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {
        if (principal != null) return "redirect:/elections/dashboard";

        if (error != null) {
            model.addAttribute("message", "Invalid email or password");
            model.addAttribute("messageType", "danger");
        } else if (logout != null) {
            model.addAttribute("message", "You have been logged out");
            model.addAttribute("messageType", "alert");
        }

        model.addAttribute("userDTO", new UserDTO());
        return "user/login";
    }

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user/register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam("confirmPassword") String confirmPassword,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        try {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
                }
            if (!password.equals(confirmPassword))
                throw new IllegalArgumentException("Passwords do not match.");
            // Check if user already exists (optional)
            if(userService.existsByUsername(name))
                throw new IllegalArgumentException("Username already exists.");
            if (userService.existsByEmail(email))
                throw new IllegalArgumentException("Email already registered.");

            User newUser = new Guider(name, email, Role.GUIDER, password);
            userService.createUser(newUser);

            redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("messageType", "danger");
            return "user/register";
        }
    }
}