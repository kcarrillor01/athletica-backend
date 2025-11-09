package com.athletica.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDto {

  @NotBlank(message = "El email es requerido")
  @Email(message = "Email inválido")
  private String email;

  @NotBlank(message = "La contraseña es requerida")
  @Size(min = 6, max = 128, message = "La contraseña debe tener al menos 6 caracteres")
  private String password;

  public LoginDto() {
  }

  public LoginDto(@NotBlank(message = "El email es requerido") @Email(message = "Email inválido") String email,
      @NotBlank(message = "La contraseña es requerida") @Size(min = 6, max = 128, message = "La contraseña debe tener al menos 6 caracteres") String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  
}
