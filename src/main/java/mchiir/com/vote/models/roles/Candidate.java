package mchiir.com.vote.models.roles;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.models.User;
import mchiir.com.vote.models.enums.Role;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Candidate extends User {
    @ManyToOne
    @JoinColumn(name = "election_id", nullable = true)
    private Election election;

    @Column(nullable = true)
    private String party;

    @Column(nullable = true)
    private String post;

    @Column(nullable = true)
    private int votes = 0;

    public Candidate(String name, String email, Role role, Election election, String party, String post) {
        super(name, email, role);
        this.election = election;
        this.party = party;
        this.post = post;
        this.votes = 0;
    }

    public Candidate(String name, String email, Election election, String post) {
        super(name, email);
        this.election = election;
        this.party = "";
        this.post = post;
        this.votes = 0;
    }
    // Getters and setters (omitted for brevity)
}
