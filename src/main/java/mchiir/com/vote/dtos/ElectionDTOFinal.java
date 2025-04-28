package mchiir.com.vote.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mchiir.com.vote.models.enums.ElectionStatus;
import mchiir.com.vote.models.roles.Candidate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ElectionDTOFinal implements Serializable {
    @NotNull
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private List<Candidate> candidates;

    @NotNull
    private ElectionStatus status;

    @NotBlank
    private String formatedStartTime;
    private String formatedEndTime;
}
