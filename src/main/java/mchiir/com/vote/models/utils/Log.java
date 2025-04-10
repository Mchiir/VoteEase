package mchiir.com.vote.models.utils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mchiir.com.vote.models.User;
import mchiir.com.vote.models.enums.ActionType;
import java.util.Date;

@Entity
@Table(name = "logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String details;

    @Column(nullable = false)
    private Date timestamp;

    @Override
    public String toString() {
        return String.format("AdminAction [id=%s, admin=%s, actionType=%s, timestamp=%s, details=%s]",
                id, user.getName(), actionType, timestamp, details);
    }
}