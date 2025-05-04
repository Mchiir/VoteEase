package mchiir.com.vote.controllers;

import mchiir.com.vote.dtos.UserDTO;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.repositories.GuiderRepository;
import mchiir.com.vote.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private final UserService userService;
    @Autowired
    private GuiderRepository guiderRepository;

    public UserController(UserService userService) {
        this.userService =  userService;
    }

    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) boolean error,
                        @RequestParam(value = "messageType", required = false) String messageType,
                        @RequestParam(value = "message", required = false) String message) {
        if (error) {
            model.addAttribute("message", message);
            model.addAttribute("messageType", (messageType != null) ? messageType : "danger");
        }

        model.addAttribute("userDTO", new UserDTO());
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/api/auth/login?error=true&message=You're not logged in in yet";
        }

        model.addAttribute("message", "You have been logged out!");
        model.addAttribute("messageType", "info");

        return "user/login";
    }

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user/register";
    }
    @PostMapping(value = "/register")
    public String register(
            @ModelAttribute Guider guider,
            RedirectAttributes redirectAttributes,
                           Model model) {
        try {
            guider.setPassword(passwordEncoder.encode(guider.getPassword()));
            userService.createUser(guider);

            redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/api/auth/login";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("messageType", "danger");
            return "user/register";
        }
    }
}