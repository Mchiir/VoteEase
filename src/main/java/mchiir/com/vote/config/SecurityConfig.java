package mchiir.com.vote.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Dotenv dotenv;
    public SecurityConfig() {
        dotenv = Dotenv.configure().load();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll() // Requires authentication for Swagger docs
                        .anyRequest().authenticated() // Allow other requests without authentication
                )
                .httpBasic(Customizer.withDefaults()) // Enables basic authentication
                .csrf(csrf -> csrf.disable()); // CSRF disabled for simplicity

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        String username = dotenv.get("SPRING_USERNAME");
        String password = dotenv.get("SPRING_PASSWORD");

        if (username == null || password == null) {
            throw new IllegalArgumentException("Environment variables SPRING_SECURITY_USERNAME or SPRING_SECURITY_PASSWORD not set.");
        }

        // Create a user with credentials from environment variables
        UserDetails user = User.builder()
                .username(username) // Use the username from .env
                .password(passwordEncoder().encode(password)) // Encode the password from .env
                .roles("USER") // Assign roles as needed
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}