package mchiir.com.vote.dtos;

import java.util.Date;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String role; // e.g., "VOTER"
    private Date createdAt;
    private Boolean deleted;
}