package mchiir.com.vote.dtos;

import java.util.Date;
import java.util.UUID;

public class VoteDTO {
    private UUID id;
    private UUID voterId;
    private UUID candidateId;
    private UUID electionId;
    private Date votedAt;
}
