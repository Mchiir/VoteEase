package mchiir.com.vote.services;

import mchiir.com.vote.models.utils.Election;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ElectionService {

    List<Election> getAllElections();

    void createElection(Election election);

    Election updateElection(UUID id, Election election);

    void deleteElection(UUID id);

    Election getElectionById(UUID id);
}
