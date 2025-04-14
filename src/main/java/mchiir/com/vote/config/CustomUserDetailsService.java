package mchiir.com.vote.config;

import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Springsecurity passed Email : " + email);

        Guider guider = (Guider) userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found or not a guider"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(guider.getName()) // or .getName() if required
                .password(guider.getPassword())
                .roles(guider.getRole().name())
                .build();
    }
}