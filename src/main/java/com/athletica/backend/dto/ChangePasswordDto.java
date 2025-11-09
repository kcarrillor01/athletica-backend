package com.athletica.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDto {

  @NotBlank(message = "El correo electrónico es requerido")
  @Email(message = "El correo electrónico debe ser válido")
  private String email;

  @NotBlank(message = "La contraseña actual es requerida")
  private String currentPassword;

  @NotBlank(message = "La nueva contraseña es requerida")
  @Size(min = 6, max = 128, message = "La contraseña debe tener al menos 6 caracteres")
  private String newPassword;

  public ChangePasswordDto() {
  }

  public ChangePasswordDto(String email, String currentPassword, String newPassword) {
    this.email = email;
    this.currentPassword = currentPassword;
    this.newPassword = newPassword;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
