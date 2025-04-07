package mchiir.com.vote.models.roles;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mchiir.com.vote.models.utils.Election;
import mchiir.com.vote.models.User;
import mchiir.com.vote.models.enums.Role;
import mchiir.com.vote.models.enums.RoleConfirmation;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Voter extends User {
    @ManyToOne
    @JoinColumn(name = "election_id", nullable = true)
    private Election election;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private RoleConfirmation isConfirmed;

    @Column(nullable = true)
    private Boolean hasVoted;

    public Voter(String name, String email, Role role, Election election) {
        super(name, email, role);
        this.election = election;
        this.isConfirmed = RoleConfirmation.PENDING;
        this.hasVoted = false;
    }
}
