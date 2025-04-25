package mchiir.com.vote.services;

import mchiir.com.vote.models.roles.Candidate;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.utils.Election;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CandidateService {
    void createCandidate(Candidate candidate);
    Candidate getCandidateById(UUID candidateId);
    List<Candidate> getAllCandidates();
    void updateCandidate(UUID candidateId, Candidate newCandidate);

    void deleteCandidate(UUID candidateId);
    List<Candidate> getCandidatesByElection(Election election);

//    boolean validateCandidate(Candidate candidate);
//    List<Candidate> searchCandidates(String keyword);
}
