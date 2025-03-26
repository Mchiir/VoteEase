package mchiir.com.vote.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a vote cast by a voter in a specific election.
 * Associates the voter, election, and candidate.
 */

@Entity
@Table(name = "votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"voter_id", "election_id"})
})
public class Vote {

    // Unique identifier for the vote
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    // The voter who cast the vote
    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private User voter;

    // The election in which the vote was cast
    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    // The candidate for whom the vote was cast
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    // Timestamp when the vote was cast
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Automatically set the vote timestamp before persisting
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // Getters and setters (omitted for brevity)
}
