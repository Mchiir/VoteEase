package mchiir.com.vote.services.impl;

import mchiir.com.vote.exceptions.ResourceNotFoundException;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.repositories.ElectionRepository;
import mchiir.com.vote.services.ElectionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository electionRepository;
    public ElectionServiceImpl(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    @Override
    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    @Override
    public List<Election> getAllByGuider(Guider guider) {
        return electionRepository.findAllByGuider(guider);
    }

    @Override
    public void createElection(Election election) {
        if (election.getStartTime().after(election.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time.");
        }
        if(election.getStartTime().before(new Date()) || election.getEndTime().before(new Date())) {
            throw new IllegalArgumentException("Start or end time cannot be past time.");
        }
        electionRepository.save(election);
    }

    @Override
    public Election updateElection(UUID id, Election election) {
        Election existingElection = electionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Election", "id", id));

        existingElection.setTitle(election.getTitle());
        existingElection.setDescription(election.getDescription());
        existingElection.setStartTime(election.getStartTime());
        existingElection.setEndTime(election.getEndTime());
        existingElection.setStatus(election.getStatus());
        return electionRepository.save(existingElection);
    }

    @Override
    public void deleteElection(UUID id) {
        Election election = electionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Election", "id", id));

        electionRepository.delete(election);
    }

    @Override
    public Election getElectionById(UUID id) {
        return electionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Election", "id", id));
    }
}
