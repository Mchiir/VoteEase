package mchiir.com.vote.dtos;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class ElectionDTO implements Serializable {
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
}