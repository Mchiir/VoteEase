package mchiir.com.vote.dtos;

import lombok.Data;
import mchiir.com.vote.models.utils.Election;

import java.io.Serializable;
import java.util.UUID;

@Data
public class CandidateDTO implements Serializable {
    private UUID id;
    private String name;
    private String email;
    private String party = "";
    private String post;
    private Election election;
    private int votes;
}