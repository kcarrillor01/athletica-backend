package com.athletica.backend.service;

import java.util.List;

import com.athletica.backend.dto.ChangePasswordDto;
import com.athletica.backend.dto.UserDto;

public interface UserService {
  List<UserDto> getAllUsers();

  UserDto updateUser(String id, UserDto dto);

  void changePassword(String userId, ChangePasswordDto changePasswordDto);
}
