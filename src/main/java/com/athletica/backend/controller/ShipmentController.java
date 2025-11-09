package com.athletica.backend.controller;

import java.util.List;

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

import com.athletica.backend.dto.ApiResponse;
import com.athletica.backend.dto.CreateShipmentRequest;
import com.athletica.backend.dto.ShipmentDto;
import com.athletica.backend.dto.UpdateShipmentRequest;
import com.athletica.backend.service.ShipmentService;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

  private final ShipmentService shipmentService;

  public ShipmentController(ShipmentService shipmentService) {
    this.shipmentService = shipmentService;
  }

  // Admin only
  @GetMapping
  public ResponseEntity<ApiResponse<List<ShipmentDto>>> getAll(Authentication auth) {
    // TODO: verificar rol admin con @PreAuthorize o revisar authorities manualmente
    List<ShipmentDto> list = shipmentService.getAllShipments();
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", list));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ShipmentDto>> getById(@PathVariable String id, Authentication auth) {
    String userId = auth != null ? auth.getName() : null;
    boolean isAdmin = auth != null
        && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    ShipmentDto dto = shipmentService.getShipmentById(id, userId, isAdmin);
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", dto));
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<ApiResponse<List<ShipmentDto>>> getByOrder(@PathVariable String orderId, Authentication auth) {
    String userId = auth != null ? auth.getName() : null;
    boolean isAdmin = auth != null
        && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    List<ShipmentDto> list = shipmentService.getShipmentsForOrder(orderId, userId, isAdmin);
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", list));
  }

  // create (admin)
  @PostMapping
  public ResponseEntity<ApiResponse<ShipmentDto>> create(@RequestBody CreateShipmentRequest req, Authentication auth) {
    String userId = auth != null ? auth.getName() : null;
    boolean isAdmin = auth != null
        && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    ShipmentDto created = shipmentService.createShipment(req, userId, isAdmin);
    return ResponseEntity.status(201).body(new ApiResponse<>(201, "Created", created));
  }

  // update (admin)
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<ShipmentDto>> update(@PathVariable String id,
      @RequestBody UpdateShipmentRequest req, Authentication auth) {
    String userId = auth != null ? auth.getName() : null;
    boolean isAdmin = auth != null
        && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    ShipmentDto updated = shipmentService.updateShipment(id, req, userId, isAdmin);
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", updated));
  }

  // delete (admin)
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> delete(@PathVariable String id, Authentication auth) {
    String userId = auth != null ? auth.getName() : null;
    boolean isAdmin = auth != null
        && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    shipmentService.deleteShipment(id, userId, isAdmin);
    return ResponseEntity.ok(new ApiResponse<>(200, "Deleted", null));
  }
}
