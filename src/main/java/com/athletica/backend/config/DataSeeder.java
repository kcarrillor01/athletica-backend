package com.athletica.backend.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
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

@Configuration
@Profile("!prod") // ejecuta en dev por defecto
public class DataSeeder implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;

  public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, CategoryRepository categoryRepository,
      ProductRepository productRepository) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.productRepository = productRepository;
  }

  private PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void run(String... args) throws Exception {
    // Roles
    if (roleRepository.count() == 0) {
      Role userRole = new Role();
      userRole.setName("USER");
      userRole.setDescription("Usuario normal");
      roleRepository.save(userRole);

      Role adminRole = new Role();
      adminRole.setName("ADMIN");
      adminRole.setDescription("Administrador");
      roleRepository.save(adminRole);
    }

    // Admin user (solo si no existe)
    Optional<User> maybeAdmin = userRepository.findByEmail("admin@athletica.test");
    if (maybeAdmin == null || !maybeAdmin.isPresent()) {
      User adminUser = new User();
      // si tu entidad User tiene id de tipo String y NO es generado, dejamos esto; si
      // es generado por JPA omite setId:
      try {
        adminUser.setId(UUID.randomUUID().toString());
      } catch (Throwable ignored) {
        // si no existe setId (poco probable), ignoramos
      }
      adminUser.setName("Admin");
      adminUser.setEmail("admin@athletica.test");
      adminUser.setPasswordHash(passwordEncoder().encode("admin123"));
      // buscar rol ADMIN (puede devolver null si repo no encuentra)
      Role adminRole = roleRepository.findByName("ADMIN");
      adminUser.setRole(adminRole);
      adminUser.setCreatedAt(LocalDateTime.now());
      adminUser.setUpdatedAt(LocalDateTime.now());
      adminUser.setIsActive(true);

      userRepository.save(adminUser);
    }

    // Category + Products sample
    if (categoryRepository.count() == 0) {
      Category cat = new Category();
      // si tu entidad usa id String no generado:
      try {
        cat.setId(UUID.randomUUID().toString());
      } catch (Throwable ignored) {
      }
      cat.setName("Ropa Deportiva");
      cat.setSlug("ropa-deportiva");
      cat.setCreatedAt(LocalDateTime.now());
      categoryRepository.save(cat);

      Product p = new Product();
      try {
        p.setId(UUID.randomUUID().toString());
      } catch (Throwable ignored) {
      }
      p.setTitle("Camiseta Athletica");
      p.setDescription("Camiseta deportiva");
      p.setPrice(BigDecimal.valueOf(29.99));
      p.setStock(100);
      p.setActive(true);
      p.setCategory(cat);
      p.setCreatedAt(LocalDateTime.now());
      p.setUpdatedAt(LocalDateTime.now());
      productRepository.save(p);
    }
  }
}
