package mchiir.com.vote.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Represents an election with details such as title, description, and voting period.
 */
@Entity
@Table(name = "elections")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
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
    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    // When voting ends
    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL)
    private List<Candidate> candidates;

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL)
    private List<Voter> voters;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    // Current status of the election (UPCOMING, ONGOING, CLOSED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ElectionStatus status = ElectionStatus.UPCOMING;

    // Getters and setters
}
