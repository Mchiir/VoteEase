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
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class User {

    // Unique identifier for the user (UUID)
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    // Full name of the user
    @Column(nullable = false)
    private String name;

    // Unique email for login
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    // Role of the user (VOTER or ADMIN)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Indicates whether the voter has cast their vote
    @Column(name = "is_voted")
    private Boolean isVoted = false;

    // Timestamp of when the account was created
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "home_address")
    private String homeAddress;

    public User(String name, String email, String password, Role role, Boolean isVoted, String homeAddr) {
        this.name = name;
        this.email = email;
        this.setPassword(password);
        this.role = role;
        this.isVoted = isVoted;
        this.homeAddress = homeAddr;
    }

    // Automatically set the creation timestamp before persisting
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
                id, name, email, password, role, isVoted, (homeAddress != null ? homeAddress.toString() : "No Address Provided")
        );
    }

}
