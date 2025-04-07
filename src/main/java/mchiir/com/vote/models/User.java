package mchiir.com.vote.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mchiir.com.vote.models.enums.Role;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a user in the online voting system.
 * Can be a voter or an administrator.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private String name;// full-name

    // Unique email for login
    @Column(nullable = false, unique = false)
    private String email;// same email due to many elections with same participants

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false) // 🛠️ Prevent duplicate column mapping
    private Role role;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(nullable = true)
    private Boolean deleted;

    public User(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.deleted = false;
    }
    public User(String name, String email) {
        this(name, email,Role.VOTER);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @Override
    public String toString() {
        return String.format(
                "User id=%s, name=%s, email=%s, role=%s",
                id, name, email, role);
    }
}
