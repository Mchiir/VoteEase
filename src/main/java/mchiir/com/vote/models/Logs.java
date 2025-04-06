package mchiir.com.vote.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Logs {  // or you can keep the name as AdminAction if preferred

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;  // Admin who performed the action

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;  // Type of action (e.g., "Created Election", "Confirmed Voter")

    @Column(nullable = false, columnDefinition = "TEXT")
    private String details;  // Detailed information about the action

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;  // When the action was performed

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();  // Automatically set the timestamp before saving
    }

    @Override
    public String toString() {
        return String.format("AdminAction [id=%s, admin=%s, actionType=%s, timestamp=%s, details=%s]",
                id, admin.getName(), actionType, timestamp, details);
    }
}