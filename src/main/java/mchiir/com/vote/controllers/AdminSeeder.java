package mchiir.com.vote.controllers;

import mchiir.com.vote.models.roles.Admin;
import mchiir.com.vote.models.enums.Role;
import mchiir.com.vote.repositories.ElectionRepository;
import mchiir.com.vote.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository adminRepository;
    @Autowired
    private ElectionRepository electionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0) {
            // the admin account
            Admin admin = new Admin();
            admin.setName("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword("admin@123");
            admin.setRole(Role.ADMIN);

            adminRepository.save(admin);
            System.out.println("Admin account created successfully!\n"+ admin);
        }
    }
}
