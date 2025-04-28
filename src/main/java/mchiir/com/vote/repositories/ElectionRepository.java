package mchiir.com.vote.repositories;

import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElectionRepository extends JpaRepository<Election, UUID> {
    List<Election> findAllByGuider(Guider guider);
    boolean existsByOtc(String otc);
    Optional<Election> findByOtc(String otc);
}
