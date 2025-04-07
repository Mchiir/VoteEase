package mchiir.com.vote.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class LogDTO implements Serializable {
    private UUID id;
    private String userName;
    private String actionType;
    private String details;
    private Date timestamp;
}
