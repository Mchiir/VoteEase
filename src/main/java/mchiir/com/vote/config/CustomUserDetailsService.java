package mchiir.com.vote.config;

import mchiir.com.vote.models.roles.Guider;
import mchiir.com.vote.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Springsecurity passed Username : " + username);

        mchiir.com.vote.models.User savedUser = userRepository.findByName(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Guider savedGuider = (Guider) savedUser;
        return User.builder()
                .username(savedGuider.getName())
                .password(savedGuider.getPassword())
                .roles(savedGuider.getRole().name())
                .build();
    }
}