package mchiir.com.vote.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a historical record of a completed election.
 * Stores key details, including the winner and participation rate.
 */
@Entity
@Table(name = "election_history")
public class ElectionHistory {

    // Unique identifier for the election history record
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    // Reference to the Election that was held
    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    // Title of the election (copied from Election)
    @Column(nullable = false)
    private String title;

    // Total votes cast in the election
    @Column(name = "total_votes", nullable = false)
    private int totalVotes;

    // Reference to the winning candidate
    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = false)
    private Candidate winner;

    // Name of the winning candidate (copied from Candidate)
    @Column(name = "winner_name", nullable = false)
    private String winnerName;

    // Number of votes received by the winning candidate
    @Column(name = "winning_votes", nullable = false)
    private int winningVotes;

    // Percentage of registered voters who participated in the election
    @Column(name = "participation_rate", nullable = false)
    private double participationRate;

    // When the election started
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    // When the election ended
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    // Getters and setters (omitted for brevity)
}
