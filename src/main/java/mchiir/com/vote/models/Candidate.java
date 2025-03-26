package mchiir.com.vote.models;

import jakarta.persistence.*;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Represents a candidate contesting in an election.
 * Contains candidate details and tracks the number of votes received.
 */
@Entity
@Table(name = "candidates")
public class Candidate {

    // Unique identifier for the candidate
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    // Candidate's full name
    @Column(nullable = false)
    private String name;

    // Political party or affiliation (if applicable)
    private String party;

    // Many candidates can belong to one election
    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    // Number of votes received by the candidate
    @Column(name = "votes_count")
    private int votesCount = 0;

    // Getters and setters (omitted for brevity)
}
