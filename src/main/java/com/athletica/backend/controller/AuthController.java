package com.athletica.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athletica.backend.dto.LoginDto;
import com.athletica.backend.dto.RegisterDto;
import com.athletica.backend.model.User;
import com.athletica.backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterDto dto) {
    User user = authService.register(dto.getName(), dto.getEmail(), dto.getPassword());
    // En producción deberías devolver un DTO seguro (sin password) o token
    return ResponseEntity.ok(user);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginDto dto) {
    User user = authService.authenticate(dto.getEmail(), dto.getPassword());
    return ResponseEntity.ok(user);
  }
}
