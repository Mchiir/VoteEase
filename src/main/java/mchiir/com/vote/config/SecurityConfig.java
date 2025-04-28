package mchiir.com.vote.config;

import mchiir.com.vote.content.MyAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyAppUserService appUserService;
    public SecurityConfig(MyAppUserService appUserService) {
        this.appUserService = appUserService;
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> {
                    httpForm.loginPage("/api/auth/login").permitAll();
                    httpForm.defaultSuccessUrl("/api/elections/dashboard", true);
                    httpForm.usernameParameter("email");
                    httpForm.passwordParameter("password");
                    httpForm.failureUrl("/api/auth/login?error=true&message=Please provide correct cridentials");
                    httpForm.permitAll();
                })
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/api/auth/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .permitAll()
                )
                .authorizeHttpRequests(registry ->{
                    registry.requestMatchers("/api/auth/**","/css/**","/js/**", "/", "/api/vote/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/api/auth/login?error=true&message=You need to log in first"))
                .build();
    }
}