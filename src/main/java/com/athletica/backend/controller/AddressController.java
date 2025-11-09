package com.athletica.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athletica.backend.dto.AddressDto;
import com.athletica.backend.dto.AddressRequest;
import com.athletica.backend.dto.ApiResponse;
import com.athletica.backend.service.AddressService;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

  private final AddressService addressService;

  public AddressController(AddressService addressService) {
    this.addressService = addressService;
  }

  /**
   * Lista solo las direcciones del usuario autenticado.
   */
  @GetMapping
  public ResponseEntity<ApiResponse<List<AddressDto>>> getMyAddresses(Authentication authentication) {
    String userId = requireAuth(authentication);
    List<AddressDto> list = addressService.getAddressesForUser(userId);
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", list));
  }

  /**
   * Obtener una dirección por id. Solo el propietario puede verla.
   */
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<AddressDto>> getAddressById(@PathVariable String id,
      Authentication authentication) {
    String userId = requireAuth(authentication);
    AddressDto dto = addressService.getAddressById(id);
    if (!userId.equals(dto.getUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new ApiResponse<>(403, "No autorizado", null));
    }
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", dto));
  }

  /**
   * Crear dirección. El userId se toma del token (authentication).
   */
  @PostMapping
  public ResponseEntity<ApiResponse<AddressDto>> createAddress(Authentication authentication,
      @RequestBody AddressRequest request) {
    String userId = requireAuth(authentication);
    request.setUserId(userId);
    AddressDto created = addressService.createAddress(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "Created", created));
  }

  /**
   * Actualizar dirección: solo propietario. No se permite cambiar el owner.
   */
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<AddressDto>> updateAddress(@PathVariable String id,
      Authentication authentication,
      @RequestBody AddressRequest request) {
    String userId = requireAuth(authentication);
    // validar propietario
    AddressDto existing = addressService.getAddressById(id);
    if (!userId.equals(existing.getUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new ApiResponse<>(403, "No autorizado", null));
    }
    request.setUserId(userId); // asegurar owner
    AddressDto updated = addressService.updateAddress(id, request);
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", updated));
  }

  /**
   * Eliminar dirección: solo propietario.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> deleteAddress(@PathVariable String id,
      Authentication authentication) {
    String userId = requireAuth(authentication);
    AddressDto existing = addressService.getAddressById(id);
    if (!userId.equals(existing.getUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new ApiResponse<>(403, "No autorizado", null));
    }
    addressService.deleteAddress(id);
    return ResponseEntity.ok(new ApiResponse<>(200, "Deleted", null));
  }

  /* ---------------- helpers ---------------- */

  private String requireAuth(Authentication authentication) {
    if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
      throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
    }
    return authentication.getName();
  }
}
