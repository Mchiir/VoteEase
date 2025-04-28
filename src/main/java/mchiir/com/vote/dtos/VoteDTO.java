package mchiir.com.vote.dtos;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class VoteDTO {
    private UUID id;
    private UUID candidateId;
    private UUID electionId;
    private Date votedAt;
}
