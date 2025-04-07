package mchiir.com.vote.dtos;

import mchiir.com.vote.models.utils.Election;

import java.util.UUID;

public class VoterDTO {
    private UUID id;
    private String name;
    private String email;
    private Election election;
    private boolean hasVoted;
    private boolean isConfirmed;
}
