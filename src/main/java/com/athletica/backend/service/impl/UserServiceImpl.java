package com.athletica.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.athletica.backend.dto.ChangePasswordDto;
import com.athletica.backend.dto.UserDto;
import com.athletica.backend.exception.BaseException;
import com.athletica.backend.model.Role;
import com.athletica.backend.model.User;
import com.athletica.backend.repository.RoleRepository;
import com.athletica.backend.repository.UserRepository;
import com.athletica.backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // Método para convertir de User a UserDto
  private UserDto toDto(User user) {
    String roleName = user.getRole() != null ? user.getRole().getName() : null;
    return new UserDto(
        user.getId(),
        user.getName(),
        user.getEmail(),
        roleName,
        user.getIsActive());
  }

  // Implementación de getAllUsers
  @Override
  public List<UserDto> getAllUsers() {
    List<User> users = userRepository.findAll(); // Obtener todos los usuarios desde la base de datos
    return users.stream() // Convertir a una lista de UserDto
        .map(this::toDto) // Mapeamos cada usuario a un UserDto
        .collect(Collectors.toList()); // Convertimos el stream a lista
  }

  // Método para actualizar usuario
  @Override
  public UserDto updateUser(String id, UserDto dto) {
    // Busca el usuario
    User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Actualiza los campos del usuario si están presentes en el DTO
    if (dto.getName() != null)
      user.setName(dto.getName());
    if (dto.getEmail() != null)
      user.setEmail(dto.getEmail());

    // Actualiza el rol
    if (dto.getRoleName() != null) {
      Role role = roleRepository.findByName(dto.getRoleName());
      if (role == null) {
        throw new RuntimeException("Rol no válido");
      }
      user.setRole(role);
    }

    // Guarda el usuario actualizado
    userRepository.save(user);

    return toDto(user);
  }

  @Override
  public void changePassword(String email, ChangePasswordDto changePasswordDto) {
    // Busca el usuario por correo electrónico
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new BaseException(404, "Usuario no encontrado"));

    // Verifica si la contraseña actual es correcta
    if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPasswordHash())) {
      throw new BaseException(400, "La contraseña actual es incorrecta");
    }

    // Si es correcta, actualiza la contraseña
    user.setPasswordHash(passwordEncoder.encode(changePasswordDto.getNewPassword()));

    // Guarda el usuario con la nueva contraseña
    userRepository.save(user);
  }

}
