package com.mashreq.room.conference.booking.config;

import com.mashreq.room.conference.booking.filter.JwtRequestFilter;
import com.mashreq.room.conference.booking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/rooms/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.POST,"/maintenance-timing/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/maintenance-timing/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/maintenance-timing/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/maintenance-timing/**").hasAnyRole("USER", "ADMIN")


                        .requestMatchers(HttpMethod.POST,"/bookings/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/bookings/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/bookings/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/bookings/**").hasAnyRole("USER", "ADMIN")


                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}