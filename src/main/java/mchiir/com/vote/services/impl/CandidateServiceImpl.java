package mchiir.com.vote.services.impl;

import lombok.AllArgsConstructor;
import mchiir.com.vote.exceptions.ResourceNotFoundException;
import mchiir.com.vote.models.roles.Candidate;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.repositories.CandidateRepository;
import mchiir.com.vote.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CandidateServiceImpl implements CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public void createCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    @Override
    public Candidate getCandidateById(UUID candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", candidateId));
    }

    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public void updateCandidate(UUID candidateId, Candidate newCandidate) {
        Candidate existingCandidate = getCandidateById(candidateId);

        existingCandidate.setName(newCandidate.getName());
        existingCandidate.setEmail(newCandidate.getEmail());
        existingCandidate.setElection(newCandidate.getElection());
        existingCandidate.setPost(newCandidate.getPost());
        existingCandidate.setParty(newCandidate.getParty());
        existingCandidate.setVotes(newCandidate.getVotes());

        candidateRepository.save(existingCandidate);
    }

    @Override
    public void deleteCandidate(UUID candidateId) {
        candidateRepository.deleteById(candidateId);
    }

    @Override
    public List<Candidate> getCandidatesByElection(Election election) {
        return candidateRepository.findByElection(election);
    }
}
