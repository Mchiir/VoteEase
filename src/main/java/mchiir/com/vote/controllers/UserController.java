package mchiir.com.vote.controllers;

import mchiir.com.vote.models.User;
import mchiir.com.vote.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    @ResponseBody
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public Optional<User> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

//    @GetMapping("/get")
//    @ResponseBody
//    public Optional<User> getUserByEmail(@RequestBody String email) {
//        return userService.getUserByEmail(email);
//    }

    @GetMapping("/get")
    @ResponseBody
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/byId/{id}")
    @ResponseBody
    public void deleteUserById(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

}
