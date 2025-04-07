package mchiir.com.vote.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Admin extends User {
    public Admin(String name, String email, String password, Role role) {
        super(name, email, password, role);
    }

    public Admin(String name, String email, String password) {
        super(name, email, password);
    }
}
