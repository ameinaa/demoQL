package rs.spai.demoQL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ❌ Désactiver CSRF (GraphQL + H2)
            .csrf(csrf -> csrf.disable())

            // ❌ Autoriser les frames (OBLIGATOIRE pour H2)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // ✅ Autorisations
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2/**").permitAll()        // H2 Console
                .requestMatchers("/graphql").permitAll()     // GraphQL
                .anyRequest().permitAll()
            )

            // 🔐 Basic Auth (pour mutations admin)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin123")
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
