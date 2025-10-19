package com.athletica.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {

  @NotBlank(message = "El nombre es requerido")
  @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
  private String name;

  @NotBlank(message = "El email es requerido")
  @Email(message = "Email inválido")
  @Size(max = 200, message = "El email no puede superar 200 caracteres")
  private String email;

  @NotBlank(message = "La contraseña es requerida")
  @Size(min = 6, max = 128, message = "La contraseña debe tener al menos 6 caracteres")
  private String password;
}
