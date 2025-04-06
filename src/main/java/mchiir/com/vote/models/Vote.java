package mchiir.com.vote.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a vote cast by a voter in a specific election.
 * Associates the voter, election, and candidate.
 */

@Entity
@Table(name = "votes")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Vote {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    // The voter who cast the vote
    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;

    // The candidate that the voter voted for
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    // The election in which the vote is cast
    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    // Timestamp when the vote was cast
    @Column(name = "voted_at", nullable = false)
    private LocalDateTime votedAt;

    // Ensure that a voter can only vote once in an election
    @PrePersist
    protected void onCreate() {
        votedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Vote [id=%s, voter=%s, candidate=%s, election=%s, votedAt=%s]",
                id, voter.getName(), candidate.getName(), election.getTitle(), votedAt);
    }
}
