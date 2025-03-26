package mchiir.com.vote.services;

import mchiir.com.vote.models.User;
import mchiir.com.vote.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;// extends spring JpaRepo

    // Create or Update a User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Find a user by ID
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    // Find a user by email
//    public Optional<User> getUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a user by ID
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    // Additional business logic - e.g., checking if a user exists by email
//    public boolean isUserEmailTaken(String email) {
//        return userRepository.existsByEmail(email);
//    }

    // Additional business logic - e.g., validating if a user can vote
//    public boolean canUserVote(UUID userId) {
//        Optional<User> user = userRepository.findById(userId);
//        return user.isPresent() && !user.get().getIsVoted();  // Example logic
//    }
}
