package mchiir.com.vote.repositories;

import mchiir.com.vote.models.User;
import mchiir.com.vote.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByName(String name);

    Optional<User> findByName(String name);
}