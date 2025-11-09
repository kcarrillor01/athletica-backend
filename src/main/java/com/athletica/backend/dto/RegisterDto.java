package com.athletica.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

  public RegisterDto() {
  }

  public RegisterDto(
      @NotBlank(message = "El nombre es requerido") @Size(max = 150, message = "El nombre no puede superar 150 caracteres") String name,
      @NotBlank(message = "El email es requerido") @Email(message = "Email inválido") @Size(max = 200, message = "El email no puede superar 200 caracteres") String email,
      @NotBlank(message = "La contraseña es requerida") @Size(min = 6, max = 128, message = "La contraseña debe tener al menos 6 caracteres") String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
