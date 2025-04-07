package mchiir.com.vote.repositories;

import mchiir.com.vote.models.utils.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ElectionRepository extends JpaRepository<Election, UUID> {

}
