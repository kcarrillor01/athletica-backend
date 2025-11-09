package com.athletica.backend.dto;

public class RoleDto {

  private Short id; // Usamos Short ya que estás utilizando `tinyint` en la BD
  private String name;
  private String description;

  public RoleDto() {
  }

  public RoleDto(Short id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public Short getId() {
    return id;
  }

  public void setId(Short id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
