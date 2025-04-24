package mchiir.com.vote.models.roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mchiir.com.vote.models.User;
import mchiir.com.vote.models.enums.Role;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Guider extends User {

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    public Guider(String name, String email, String password) {
        super(name, email, Role.Guider);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}