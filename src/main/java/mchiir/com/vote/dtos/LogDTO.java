package mchiir.com.vote.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class LogDTO implements Serializable {
    private UUID id;
    private String userName;
    private String actionType;
    private String details;
    private Date timestamp;
}
