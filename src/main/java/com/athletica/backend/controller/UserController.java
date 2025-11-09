package com.athletica.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athletica.backend.dto.ApiResponse;
import com.athletica.backend.dto.ChangePasswordDto;
import com.athletica.backend.dto.UserDto;
import com.athletica.backend.exception.BaseException;
import com.athletica.backend.model.Role;
import com.athletica.backend.model.User;
import com.athletica.backend.repository.UserRepository;
import com.athletica.backend.service.RoleService;
import com.athletica.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final RoleService roleService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserController(UserService userService, RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.roleService = roleService;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /** GET /api/users */
  @GetMapping
  public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
    List<UserDto> users = userService.getAllUsers();
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", users));
  }

  /** GET /api/roles */
  @GetMapping("/roles")
  public ResponseEntity<ApiResponse<List<Role>>> getAllRoles() {
    List<Role> roles = roleService.getAllRoles();
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", roles));
  }

  /** POST /api/users/{id} */
  @PostMapping("/{id}")
  public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable String id, @RequestBody UserDto dto) {
    UserDto updated = userService.updateUser(id, dto);
    return ResponseEntity.ok(new ApiResponse<>(200, "Updated", updated));
  }

  @PostMapping("/change-password")
  public void changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
    // Busca al usuario por correo electrónico
    User user = userRepository.findByEmail(changePasswordDto.getEmail())
        .orElseThrow(() -> new BaseException(404, "Usuario no encontrado"));

    // Verifica si la contraseña actual es correcta
    if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPasswordHash())) {
      throw new BaseException(400, "La contraseña actual es incorrecta");
    }

    // Si la contraseña actual es correcta, actualiza la contraseña
    user.setPasswordHash(passwordEncoder.encode(changePasswordDto.getNewPassword()));

    // Guarda el usuario con la nueva contraseña
    userRepository.save(user);
  }

}
