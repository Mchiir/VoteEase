package mchiir.com.vote.services.impl;

import lombok.AllArgsConstructor;
import mchiir.com.vote.dtos.CandidateResult;
import mchiir.com.vote.dtos.ElectionResultDTO;
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
import java.util.stream.Collectors;

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
            throw new IllegalArgumentException("Invalid code format: must be 7 characters");
        }

        Election election = electionRepository.findByOtc(otc)
                .orElseThrow(() -> new ResourceNotFoundException("Election", "otc", otc));

        if(election.getStatus() == ElectionStatus.CLOSED)
            throw new IllegalArgumentException("The election has ended.");
        if(election.getStatus() == ElectionStatus.UPCOMING)
            throw new IllegalArgumentException("The election not yet started.");

        return election;
    }

    @Override
    @Transactional
    public void saveVote(VoteDTOFinal voteDTO) {
            // Step 1: Retrieve the election using the electionId from the voteDTO
            Election election = electionRepository.findById(voteDTO.getElectionId())
                    .orElseThrow(() -> new RuntimeException("Election not found"));

            int maxVoters = election.getMax_voters_count();
            int current_voters = election.getVoters_count();
            if(current_voters >= maxVoters) {
                throw new IllegalStateException("Maximum number of voters has been reached for this election.");
            }

            election.setVoters_count(current_voters + 1);

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
    }

    @Override
    public ElectionResultDTO getElectionResult(UUID electionId) {
        Map<String, List<CandidateResult>> postsWithCandidates = new HashMap<>();

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found"));

        if(election.getStatus() == ElectionStatus.ONGOING)
            throw new SecurityException("The election is now in progress, let it end first");
        if(election.getStatus() == ElectionStatus.UPCOMING)
            throw new SecurityException("The election has not yet started");
        if(election.getStatus() != ElectionStatus.CLOSED)
            throw new SecurityException("The election has not yet ended");

        List<Candidate> candidates = election.getCandidates();

        // Get all unique posts
        Set<String> allPosts = candidates.stream()
                .map(Candidate::getPost)
                .collect(Collectors.toSet());

        for (String post : allPosts) {
            List<CandidateResult> candidateResults = candidates.stream()
                    .filter(c -> c.getPost().equals(post))
                    .map(c -> new CandidateResult(c.getId(), c.getName(), c.getVotes()))
                    .sorted(Comparator.comparingInt(CandidateResult::getVoteCount).reversed())
                    .collect(Collectors.toList());

            // top candidate is winner if the list is not empty
            if (!candidateResults.isEmpty()) {
                candidateResults.get(0).setWinner(true);
            }

            postsWithCandidates.put(post, candidateResults);
        }

        // Build and return DTO
        ElectionResultDTO dto = new ElectionResultDTO();
        dto.setTitle(election.getTitle());
        dto.setDescription(election.getDescription());
        dto.setFormatedStartTime(election.getFormatedStartTime());
        dto.setFormatedEndTime(election.getFormatedEndTime());
        dto.setStatus(election.getStatus().toString());
        dto.setMaxVotersCount(election.getMax_voters_count());
        dto.setVotersCount(election.getVoters_count());
        dto.setGuiderName(election.getGuider().getName()); // Adjust if different
        dto.setPostsWithCandidates(postsWithCandidates);

        return dto;
    }
}
