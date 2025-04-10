package mchiir.com.vote.controllers;

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

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService =  userService;
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/elections/dashboard";  // Redirect to dashboard if already logged in
        }
        model.addAttribute("userDTO", new UserDTO());
        return "user/login";  // return the login page
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
                           Model model) {
        try {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }

            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Passwords do not match.");
            }

            // Check if user already exists (optional)
            if (userService.existsByEmail(email)) {
                throw new IllegalArgumentException("Email already registered.");
            }

            User newUser = new Guider(name, email, Role.GUIDER, password);
            userService.createUser(newUser);

            model.addAttribute("message", "Registration successful. Please log in.");
            model.addAttribute("messageType", "success");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("messageType", "danger");
            return "user/register";
        }
    }
}