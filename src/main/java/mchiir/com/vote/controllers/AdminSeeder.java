package mchiir.com.vote.controllers;

import mchiir.com.vote.models.roles.Admin;
import mchiir.com.vote.models.enums.Role;
import mchiir.com.vote.repositories.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0) {
            // the admin account
            Admin admin = new Admin();
            admin.setName("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin@123"));
            admin.setRole(Role.Admin);

            adminRepository.save(admin);
            System.out.println("Admin account created successfully!\n"+ admin);
        }
    }
}
