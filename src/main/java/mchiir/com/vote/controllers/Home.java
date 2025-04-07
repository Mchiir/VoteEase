package mchiir.com.vote.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the Online Voting System!");
        return "home";
    }
}
