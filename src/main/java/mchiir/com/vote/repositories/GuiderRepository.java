package mchiir.com.vote.repositories;

import mchiir.com.vote.models.roles.Guider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuiderRepository extends JpaRepository<Guider, UUID> {
    Optional<Guider> findByEmail(String email);
    Optional<Guider> findByName(String name);
}
