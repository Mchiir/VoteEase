package mchiir.com.vote.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;

@Data
public class ElectionDTO implements Serializable {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 10, message = "Number of voters must be at least 10")
    @Max(value = 500, message = "Number of voters must be at max 500")
    private int maxVotersCount;
}