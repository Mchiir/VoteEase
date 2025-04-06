package mchiir.com.vote.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
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

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private RoleConfirmation confirmation;

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.setPassword(password);
        this.role = role;
        this.deleted = false;
        confirmation= RoleConfirmation.PENDING;
    }
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.setPassword(password);
        role = Role.VOTER;
        this.deleted = false;
        confirmation= RoleConfirmation.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    @Override
    public String toString() {
        return String.format(
                "User id=%s, name=%s, email=%s, password=%s, role=%s, isVoted=%s, homeAddress=%s",
                id, name, email, password, role);
    }
}
