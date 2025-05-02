package mchiir.com.vote.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class ElectionDTO implements Serializable {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 10, message = "Number of voters must be at least 10")
    @Max(value = 500, message = "Number of voters must be at max 500")
    private int maxVotersCount;

    @NotEmpty(message = "At least one post must be provided")
    private List<String> posts;

    private List<String> parties; // Optional, so no constraint
}