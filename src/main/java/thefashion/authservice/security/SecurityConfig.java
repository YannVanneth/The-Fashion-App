package thefashion.authservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  {

        //Make All Endpoints catch by security
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
        );

        //disable CSRF
        http.csrf(csrf -> csrf.disable());

        //Disable form login
        http.formLogin(formLogin -> formLogin.disable());

        //Disable HTTP Basic
        http.httpBasic(httpBasic -> httpBasic.disable());

        //make stateless session
        http.sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Add JWT filter for getting user details by using token
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
