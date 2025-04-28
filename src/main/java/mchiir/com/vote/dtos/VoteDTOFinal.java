package mchiir.com.vote.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
public class VoteDTOFinal implements Serializable {
    @NotNull
    private UUID electionId;

    @NotEmpty
    private Map<@NotBlank String,@NotNull UUID> candidateVotes; // Key: Post name, Value: Candidate ID
}
