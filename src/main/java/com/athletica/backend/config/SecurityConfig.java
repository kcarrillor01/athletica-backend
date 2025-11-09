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
@EnableMethodSecurity // habilita @PreAuthorize si lo usas en controllers
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
            // excepciones públicas
            .requestMatchers("/error").permitAll()
            .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/favicon.ico", "/static/**", "/public/**").permitAll()

            // reglas específicas: recursos de administración (ponerlas antes de reglas
            // genéricas)
            .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

            // permitir a usuarios autenticados operar sobre sus direcciones
            .requestMatchers(HttpMethod.POST, "/api/addresses/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/addresses/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/addresses/**").hasAnyRole("USER", "ADMIN")

            // pedidos: permitir crear pedidos a usuarios autenticados
            .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAnyRole("USER", "ADMIN")
            // si quieres permitir ver mis pedidos por /api/orders/my (propietario) lo
            // manejas en controller
            // para todo lo demás de /api/orders (GET /api/orders) ya está restringido a
            // ADMIN arriba

            // Reglas generales al final: lectura para usuarios autenticados, otras acciones
            // por role
            .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
            // fallback: cualquier otra petición autenticada
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
