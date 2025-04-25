package mchiir.com.vote.repositories;

import mchiir.com.vote.models.roles.Candidate;
import mchiir.com.vote.models.utils.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    List<Candidate> findByElection(Election election);
}
