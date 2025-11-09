package com.athletica.backend.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.athletica.backend.model.User;
import com.athletica.backend.repository.RoleRepository;
import com.athletica.backend.repository.UserRepository;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  // Constructor explícito -> evita depender de Lombok
  public AuthService(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  public User register(String name, String email, String password) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("Email ya registrado");
    }

    User u = new User();
    // Si tu entidad genera el id automáticamente con JPA, puedes quitar setId(...)
    u.setId(UUID.randomUUID().toString());
    u.setName(name);
    u.setEmail(email);
    u.setPasswordHash(passwordEncoder.encode(password));
    u.setRole(roleRepository.findByName("USER"));
    u.setCreatedAt(LocalDateTime.now());
    u.setUpdatedAt(LocalDateTime.now());
    // usa el setter correcto según tu entidad (tu DTO usa setIsActive)
    try {
      u.setIsActive(true);
    } catch (NoSuchMethodError | NoClassDefFoundError e) {
      // si no existe setIsActive, intenta setActive (por seguridad)
      try {
        u.getClass().getMethod("setActive", Boolean.class).invoke(u, true);
      } catch (Throwable ignored) {
      }
    }

    return userRepository.save(u);
  }

  public User authenticate(String email, String password) {
    var userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
      throw new IllegalArgumentException("Credenciales inválidas");
    }
    User u = userOpt.get();
    if (!passwordEncoder.matches(password, u.getPasswordHash())) {
      throw new IllegalArgumentException("Credenciales inválidas");
    }
    u.setLastLogin(LocalDateTime.now());
    userRepository.save(u);
    return u;
  }
}
