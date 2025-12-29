package rs.spai.demoQL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	
	@Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable());

	    http.authorizeHttpRequests(auth -> auth
	        .requestMatchers("/graphql").authenticated()
	        .anyRequest().permitAll()
	    ).httpBasic(Customizer.withDefaults());

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
