package mchiir.com.vote.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ElectionResultDTO {
    private String title;
    private String description;
    private String formatedStartTime;
    private String formatedEndTime;
    private String status;
    private int maxVotersCount;
    private int votersCount;
    private String guiderName; // From the Guider entity

    // Key: Post name, Value: List of candidates for that post
    private Map<String, List<CandidateResult>> postsWithCandidates;
}
