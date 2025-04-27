package mchiir.com.vote.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO implements Serializable {
    private String name;
    private String email;
    private String party;
    private String post;
}