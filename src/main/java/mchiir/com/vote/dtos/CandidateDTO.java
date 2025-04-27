package mchiir.com.vote.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class CandidateDTO implements Serializable {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String party;

    @NotBlank
    private String post;
}