package mchiir.com.vote.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an election with details such as title, description, and voting period.
 */
@Entity
@Table(name = "elections")
public class Election {

    // Unique identifier for the election
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    // Name of the election (e.g., "Student Council Elections 2024")
    @Column(nullable = false)
    private String title;

    // Detailed description of the election
    @Column(columnDefinition = "TEXT")
    private String description;

    // When voting starts
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    // When voting ends
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    // Current status of the election (UPCOMING, ONGOING, CLOSED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ElectionStatus status = ElectionStatus.UPCOMING;

    // Getters and setters (omitted for brevity)
}
