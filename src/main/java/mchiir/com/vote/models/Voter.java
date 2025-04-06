package mchiir.com.vote.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "voters")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Voter extends User {
    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election election;

    private Boolean confirmed; // Admin confirmation required

    //getters & setters
}
