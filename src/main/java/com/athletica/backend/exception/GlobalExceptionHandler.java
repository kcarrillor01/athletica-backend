package com.athletica.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.athletica.backend.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Manejador de ResponseStatusException
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Object>> handleResponseStatus(ResponseStatusException ex) {
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    if (status == null)
      status = HttpStatus.INTERNAL_SERVER_ERROR;

    return ResponseEntity
        .status(status)
        .body(new ApiResponse<>(status.value(), ex.getReason() != null ? ex.getReason() : ex.getMessage(), null));
  }

  // Manejador de excepciones generales (Exception)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
    ex.printStackTrace(); // útil en desarrollo
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(500, "Internal Server Error", null));
  }

  // Manejador de excepciones de validación
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    StringBuilder message = new StringBuilder();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
    }
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse<>(400, message.toString(), null));
  }

  // Manejador de excepciones de violación de integridad de base de datos (por
  // ejemplo, clave única)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<Object>> handleDatabaseExceptions(DataIntegrityViolationException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse<>(400, "Error de base de datos: " + ex.getMessage(), null));
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ApiResponse<Object>> handleBaseException(BaseException ex) {
    ex.printStackTrace(); // Esto ayuda a ver si realmente se captura la excepción
    return ResponseEntity
        .status(ex.getStatusCode())
        .body(new ApiResponse<>(ex.getStatusCode(), ex.getMessage(), null));
  }

}
