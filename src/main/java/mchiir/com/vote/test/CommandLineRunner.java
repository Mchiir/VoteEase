package mchiir.com.vote.test;

import org.springframework.context.annotation.Bean;

public class CommandLineRunner {
    @Bean
    CommandLineRunner commandLineRunner() {
        return new CommandLineRunner();
    }
}
