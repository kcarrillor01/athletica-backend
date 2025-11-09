package com.athletica.backend.dto;

public class UserDto {
  private String id;
  private String name;
  private String email;
  private String roleName; // Solo el nombre del rol
  private Boolean isActive;

  public UserDto() {
  }

  public UserDto(String id, String name, String email, String roleName, Boolean isActive) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.roleName = roleName;
    this.isActive = isActive;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }
}
