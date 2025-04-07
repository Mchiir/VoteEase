package mchiir.com.vote.dtos;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String role; // e.g., "VOTER"
    private Date createdAt;
    private Boolean deleted;
}