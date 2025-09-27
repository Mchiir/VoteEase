package mchiir.com.vote.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    private String email;
    private String role; // e.g., "VOTER"
    private Date createdAt;
    private Boolean deleted;
}