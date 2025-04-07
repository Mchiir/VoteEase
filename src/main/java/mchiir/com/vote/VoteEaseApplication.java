package mchiir.com.vote;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VoteEaseApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(VoteEaseApplication.class, args);
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
