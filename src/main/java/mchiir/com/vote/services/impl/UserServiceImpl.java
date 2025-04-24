package mchiir.com.vote.services.impl;

import mchiir.com.vote.models.User;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.repositories.GuiderRepository;
import mchiir.com.vote.repositories.UserRepository;
import mchiir.com.vote.services.UserService;
import mchiir.com.vote.exceptions.ResourceNotFoundException; // Make sure this is imported
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final GuiderRepository guiderRepository;

    public UserServiceImpl(UserRepository userRepository, GuiderRepository guiderRepository) {
        this.guiderRepository = guiderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Guider createUser(Guider guider) {
        return guiderRepository.save(guider);
    }

    @Override
    public User updateUser(UUID id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByName(username);
    }

    @Override
    public Guider findByEmail(String email) {
        return guiderRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public Guider findByUsername(String username) {
        return guiderRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Guider", "name", username));
    }
}