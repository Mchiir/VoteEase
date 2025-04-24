package mchiir.com.vote.controllers;

import io.github.cdimascio.dotenv.Dotenv;
import mchiir.com.vote.dtos.UserDTO;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.repositories.GuiderRepository;
import mchiir.com.vote.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Dotenv dotenv;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private final UserService userService;
    @Autowired
    private GuiderRepository guiderRepository;

    public UserController(UserService userService) {
        this.userService =  userService;
        dotenv = Dotenv.load();
    }

    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "message", required = false) String message,
                        @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("message", message);
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