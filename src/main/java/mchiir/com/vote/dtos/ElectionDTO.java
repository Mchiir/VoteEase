package mchiir.com.vote.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ElectionDTO implements Serializable {
    private UUID id;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private boolean active;
}