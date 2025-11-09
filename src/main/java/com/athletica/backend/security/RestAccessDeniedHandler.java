package com.athletica.backend.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.athletica.backend.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/json");
    ApiResponse<Object> body = new ApiResponse<>(HttpStatus.FORBIDDEN.value(),
        "Forbidden: you don't have permission to access this resource", null);
    mapper.writeValue(response.getOutputStream(), body);
  }
}
