package com.athletica.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.athletica.backend.repository.UserRepository;
import com.athletica.backend.security.JwtAuthenticationFilter;
import com.athletica.backend.security.JwtUtil;
import com.athletica.backend.security.RestAccessDeniedHandler;
import com.athletica.backend.security.RestAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  public SecurityConfig(JwtUtil jwtUtil, UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, userRepository);

    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // public
            .requestMatchers("/error").permitAll()
            .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/favicon.ico", "/static/**", "/public/**").permitAll()

            // Shipments rules:
            // - GET /api/shipments -> only ADMIN (list all shipments)
            // - GET /api/shipments/** (individual or by order) -> authenticated
            // (USER/ADMIN).
            // controller will enforce owner vs admin logic for single resources.
            // - POST /api/shipments, PUT /api/shipments/{id}, DELETE /api/shipments/{id} ->
            // ADMIN only
            .requestMatchers(HttpMethod.GET, "/api/shipments").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/shipments").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/shipments/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/shipments/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/shipments/**").hasAnyRole("USER", "ADMIN")

            // Orders: allow authenticated users to create; list all orders reserved to
            // ADMIN
            .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")

            // Addresses: allow user/admin to operate on their addresses (controller checks
            // ownership)
            .requestMatchers(HttpMethod.POST, "/api/addresses/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/addresses/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/addresses/**").hasAnyRole("USER", "ADMIN")

            // Admin-only patterns (generic)
            .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

            // Default: authenticated read for API, other methods require ADMIN
            .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .accessDeniedHandler(new RestAccessDeniedHandler()))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
