package mchiir.com.vote.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Auth {
    @GetMapping("/")
    public String home() {
        return "Hello World";
    }
}
