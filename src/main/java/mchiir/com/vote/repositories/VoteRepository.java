package mchiir.com.vote.repositories;

import mchiir.com.vote.models.utils.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

}
