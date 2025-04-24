package mchiir.com.vote.services;

import mchiir.com.vote.models.User;
import mchiir.com.vote.models.roles.Guider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    List<User> getAllUsers();

    User createUser(User user);
    Guider createUser(Guider guider);

    User updateUser(UUID id, User user);

    void deleteUser(UUID id);

    User getUserById(UUID id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Guider findByEmail(String email);
    Guider findByUsername(String username);
}