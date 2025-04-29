package mchiir.com.vote.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CandidateResult {
    private UUID id;
    private String name;
    private int voteCount;
    private boolean isWinner;

    public CandidateResult(UUID id, String name, int voteCount) {
        this.id = id;
        this.name = name;
        this.voteCount = voteCount;
        this.isWinner = false;
    }
}