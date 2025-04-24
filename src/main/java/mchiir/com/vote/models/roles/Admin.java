package mchiir.com.vote.models.roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mchiir.com.vote.models.User;
import mchiir.com.vote.models.enums.Role;

@Entity
@NoArgsConstructor
@Setter @Getter
public class Admin extends User {
    @Column(name = "password", nullable = true, length = 60)
    private String password;

    public Admin(String name, String email, Role role, String password) {
        super(name, email, role);
        this.password = password;
    }
}
