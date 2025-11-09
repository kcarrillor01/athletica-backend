package com.athletica.backend.exception;

public class ExceptionUtils {

  // Método genérico para lanzar una excepción
  public static void throwNewBaseException(int statusCode, String message) {
    throw new BaseException(statusCode, message);
  }
}
