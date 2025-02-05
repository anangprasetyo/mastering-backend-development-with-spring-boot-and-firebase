package id.backend.session_6.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.cloud.storage.HttpMethod;

import id.backend.session_6.utils.FirebaseAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Firebase Authentication Filter
    @Autowired
    private FirebaseAuthenticationFilter firebaseAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authManager -> {
                    authManager
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/api/members/**").authenticated()
                            .anyRequest().permitAll();
                })
                .csrf(csrf -> csrf.disable())
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Firebase Authentication Filter
        http.addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
