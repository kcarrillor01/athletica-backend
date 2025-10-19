package com.athletica.backend.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.athletica.backend.model.User;
import com.athletica.backend.repository.RoleRepository;
import com.athletica.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public User register(String name, String email, String password) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("Email ya registrado");
    }
    User u = User.builder()
        .id(UUID.randomUUID().toString())
        .name(name)
        .email(email)
        .passwordHash(passwordEncoder.encode(password))
        .role(roleRepository.findByName("USER"))
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .isActive(true)
        .build();
    return userRepository.save(u);
  }

  public User authenticate(String email, String password) {
    var userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty())
      throw new IllegalArgumentException("Credenciales inválidas");
    User u = userOpt.get();
    if (!passwordEncoder.matches(password, u.getPasswordHash()))
      throw new IllegalArgumentException("Credenciales inválidas");
    u.setLastLogin(LocalDateTime.now());
    userRepository.save(u);
    return u;
  }
}
