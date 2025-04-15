package mchiir.com.vote.test;

import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.repositories.UserRepository;

public class Test {
    private static UserRepository userRepository;
    public Test(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        try{
            Guider guider = (Guider) userRepository.findByEmail("admin@example.com")
                    .orElseThrow(() -> new RuntimeException("User not found"));

            System.out.println("Found user: "+ guider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}