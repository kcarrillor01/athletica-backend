package com.athletica.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athletica.backend.dto.ApiResponse;
import com.athletica.backend.dto.AuthResponse;
import com.athletica.backend.dto.LoginDto;
import com.athletica.backend.dto.RegisterDto;
import com.athletica.backend.model.User;
import com.athletica.backend.security.JwtUtil;
import com.athletica.backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtUtil jwtUtil;

  public AuthController(AuthService authService, JwtUtil jwtUtil) {
    this.authService = authService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<User>> register(@RequestBody @Valid RegisterDto dto) {
    User user = authService.register(dto.getName(), dto.getEmail(), dto.getPassword());
    ApiResponse<User> res = new ApiResponse<>(201, "Created", user);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginDto dto) {
    try {
      User user = authService.authenticate(dto.getEmail(), dto.getPassword());

      Map<String, Object> claims = new HashMap<>();
      if (user.getRole() != null && user.getRole().getName() != null) {
        claims.put("role", user.getRole().getName());
      }

      String token = jwtUtil.generateToken(user.getId(), claims);
      AuthResponse response = new AuthResponse(token, user);

      return ResponseEntity.ok(new ApiResponse<>(200, "OK", response));

    } catch (IllegalArgumentException e) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse<>(401, e.getMessage(), null));
    }
  }

}
