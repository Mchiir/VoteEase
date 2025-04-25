package mchiir.com.vote.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class ElectionDTO implements Serializable {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private Date startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private Date endTime;

    private String otc;

//    @AssertTrue(message = "End time must be after start time")
//    public boolean isEndTimeAfterStartTime() {
//        return startTime != null && endTime != null && endTime.after(startTime);
//    }
}