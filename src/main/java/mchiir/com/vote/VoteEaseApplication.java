package mchiir.com.vote;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class VoteEaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(VoteEaseApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Controller
    static class Home {
        @GetMapping("/")
        public String home(Model model) {
            model.addAttribute("message", "Welcome to the Online Voting System!");
            System.out.println("Context path URL.");
            return "home";
        }
    }
    /*
    *
    * @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            // Create an Address object
            Address address = new Address("123 Main St", "Kigali", "Gasabo", "Kacyiru", "Nyabugogo");
            User user = new User("Mugisha", "mugisha@gmail.com", "mugisha@123", Role.VOTER, false, address);
            userRepository.save(user);

            // Fetch and print user
            User myUser = userRepository.findAll().get(0);
            System.out.println(myUser);
        };
    }
    * */
}
