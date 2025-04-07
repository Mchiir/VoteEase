package mchiir.com.vote.models.roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mchiir.com.vote.models.User;
import mchiir.com.vote.models.enums.Role;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@NoArgsConstructor
public class Admin extends User {
    @Column(name = "password", nullable = true, length = 60)
    @Getter @Setter
    private String password;

    public Admin(String name, String email, Role role) {
        super(name, email, role);
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
    }
}
