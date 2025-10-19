package com.athletica.backend.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.athletica.backend.model.Category;
import com.athletica.backend.model.Product;
import com.athletica.backend.model.Role;
import com.athletica.backend.model.User;
import com.athletica.backend.repository.CategoryRepository;
import com.athletica.backend.repository.ProductRepository;
import com.athletica.backend.repository.RoleRepository;
import com.athletica.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Profile("!prod") // ejecuta en dev por defecto
public class DataSeeder implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;

  private PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void run(String... args) throws Exception {
    // Roles
    if (roleRepository.count() == 0) {
      Role user = Role.builder()
          .name("USER")
          .description("Usuario normal")
          .build();
      Role admin = Role.builder()
          .name("ADMIN")
          .description("Administrador")
          .build();
      roleRepository.save(user);
      roleRepository.save(admin);
    }

    // Admin user
    if (!userRepository.findByEmail("admin@athletica.test").isPresent()) {
      User adminUser = User.builder()
          .id(UUID.randomUUID().toString())
          .name("Admin")
          .email("admin@athletica.test")
          .passwordHash(passwordEncoder().encode("admin123"))
          .role(roleRepository.findByName("ADMIN"))
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .isActive(true)
          .build();
      userRepository.save(adminUser);
    }

    // Category + Products sample
    if (categoryRepository.count() == 0) {
      Category cat = Category.builder()
          .id(UUID.randomUUID().toString())
          .name("Ropa Deportiva")
          .slug("ropa-deportiva")
          .createdAt(LocalDateTime.now())
          .build();
      categoryRepository.save(cat);

      Product p = Product.builder()
          .id(UUID.randomUUID().toString())
          .title("Camiseta Athletica")
          .description("Camiseta deportiva")
          .price(BigDecimal.valueOf(29.99))
          .stock(100)
          .active(true)
          .category(cat)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();
      productRepository.save(p);
    }
  }
}
