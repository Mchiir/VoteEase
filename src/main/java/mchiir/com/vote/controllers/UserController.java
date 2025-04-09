package mchiir.com.vote.controllers;

import mchiir.com.vote.dtos.UserDTO;
import mchiir.com.vote.models.User;
import mchiir.com.vote.payloads.ApiResponse;
import mchiir.com.vote.services.UserService;
import mchiir.com.vote.services.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService =  userService;
    }

    // GET all users
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userResponse);
    }

    // POST create a new user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto) {
        User userRequest = modelMapper.map(userDto, User.class);
        User savedUser = userService.createUser(userRequest);
        UserDTO userResponse = modelMapper.map(savedUser, UserDTO.class);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    // PUT update a user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDto) {
        User userRequest = modelMapper.map(userDto, User.class);
        User updatedUser = userService.updateUser(id, userRequest);
        UserDTO userResponse = modelMapper.map(updatedUser, UserDTO.class);
        return ResponseEntity.ok(userResponse);
    }

    // DELETE user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        ApiResponse apiResponse = new ApiResponse(true, "User deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}