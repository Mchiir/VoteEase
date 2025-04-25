package mchiir.com.vote.models.utils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mchiir.com.vote.models.enums.ElectionStatus;
import mchiir.com.vote.models.roles.Candidate;
import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.models.roles.Voter;
import org.hibernate.annotations.GenericGenerator;

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

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(columnDefinition = "TEXT", length = 255, nullable = false)
    private String description;

    @Column(name = "otc", unique = true, length = 7)
    private String otc;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    // When voting ends
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL)
    private List<Candidate> candidates;



    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL)
    private List<Voter> voters;

    @ManyToOne
    @JoinColumn(name = "guider_id")
    private Guider guider;

    // Current status of the election (UPCOMING, ONGOING, CLOSED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ElectionStatus status = ElectionStatus.UPCOMING;

    private String formatedStartTime;
    private String formatedEndTime;

    @Column(name = "is_hidden", nullable = false)
    private boolean isHidden = false;  // Default to false (not deleted)

    public boolean getIsHidden() { return isHidden; }
    public void setIsHidden(boolean isHidden) { this.isHidden = isHidden; }
}
