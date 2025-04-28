package mchiir.com.vote.services.impl;

import lombok.AllArgsConstructor;
import mchiir.com.vote.dtos.VoteDTOFinal;
import mchiir.com.vote.exceptions.ResourceNotFoundException;
import mchiir.com.vote.models.enums.ElectionStatus;
import mchiir.com.vote.models.roles.Candidate;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.repositories.CandidateRepository;
import mchiir.com.vote.repositories.ElectionRepository;
import mchiir.com.vote.services.CandidateService;
import mchiir.com.vote.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ElectionServiceImpl implements ElectionService {

    @Autowired
    private final ElectionRepository electionRepository;
    @Autowired
    private final CandidateRepository candidateRepository;
    @Autowired
    private final CandidateService candidateService;

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
        electionRepository.save(election);
    }

    @Override
    @Transactional
    public Election updateElection(UUID id, Election election) {
        Election existingElection = electionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Election", "id", id));

        // Only update modifiable fields (defensive copy)
        existingElection.setTitle(election.getTitle());
        existingElection.setDescription(election.getDescription());
        existingElection.setStartTime(election.getStartTime());
        existingElection.setEndTime(election.getEndTime());
        existingElection.setStatus(election.getStatus());
        existingElection.setIsHidden(election.getIsHidden());

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

    @Override
    public String generateOtc(){
        String otc;
        do {
            Random random = new Random();
            int part1 = random.nextInt(1000); // 000 to 999
            int part2 = random.nextInt(1000);
            otc =  String.format("%03d-%03d", part1, part2); // e.g., 034-234
        } while (electionRepository.existsByOtc(otc)); // Check uniqueness
        return otc;
    }

    @Override
    public Election getElectionByOtc(String otc){
        if (otc == null || otc.length() != 7) {
            throw new IllegalArgumentException("Invalid election code format");
        }

        Election election = electionRepository.findByOtc(otc)
                .orElseThrow(() -> new ResourceNotFoundException("Election", "otc", otc));

        if(election.getStatus() != ElectionStatus.ONGOING)
            throw new IllegalArgumentException("The election has not yet started or ended.");

        return election;
    }

    @Override
    @Transactional
    public void saveVote(VoteDTOFinal voteDTO) {
        try {
            // Step 1: Retrieve the election using the electionId from the voteDTO
            Election election = electionRepository.findById(voteDTO.getElectionId())
                    .orElseThrow(() -> new RuntimeException("Election not found"));

            // Iterating over the candidateVotes map to update vote counts for each selected candidate
            for (Map.Entry<String, UUID> entry : voteDTO.getCandidateVotes().entrySet()) {
                String post = entry.getKey(); // The post name
                UUID candidateId = entry.getValue(); // Candidate ID

                Candidate candidate = candidateRepository.findById(candidateId)
                        .orElseThrow(() -> new RuntimeException("Candidate not found for ID: " + candidateId));

                // Increment the candidate's vote count
                candidate.setVotes(candidate.getVotes() + 1);

                // Step 5: Save the updated candidate
                candidateService.updateCandidate(candidateId, candidate);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error saving vote", e);
        }
    }
}
