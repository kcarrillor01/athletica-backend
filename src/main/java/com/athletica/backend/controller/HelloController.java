//src\main\java\com\athletica\backend\controller\HelloController.java
package com.athletica.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  @GetMapping("/api/hello")
  public String hello() {
    return "Athletica backend OK";
  }
}