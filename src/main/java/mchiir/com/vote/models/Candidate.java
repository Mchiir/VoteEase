package mchiir.com.vote.models;

import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Candidate extends User {
    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    @Column(nullable = true)
    private String party;

    @Column
    private String post;

    @Column(nullable = true)
    private int votes = 0;

    public Candidate(String name, String email, String password, Role role, Election election, String party, String post) {
        super(name, email, password, role);
        this.election = election;
        this.party = party;
        this.post = post;
        this.votes = 0;
    }

    public Candidate(String name, String email, String password, Election election, String post) {
        super(name, email, password);
        this.election = election;
        this.party = "";
        this.post = post;
        this.votes = 0;
    }
    // Getters and setters (omitted for brevity)
}
