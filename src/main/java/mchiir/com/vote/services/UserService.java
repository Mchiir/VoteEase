package mchiir.com.vote.services;

import mchiir.com.vote.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(UUID id, User user);

    void deleteUser(UUID id);

    User getUserById(UUID id);
}

