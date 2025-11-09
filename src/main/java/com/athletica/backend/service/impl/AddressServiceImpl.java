package com.athletica.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.athletica.backend.dto.AddressDto;
import com.athletica.backend.dto.AddressRequest;
import com.athletica.backend.model.Address;
import com.athletica.backend.model.City;
import com.athletica.backend.model.User;
import com.athletica.backend.repository.AddressRepository;
import com.athletica.backend.repository.CityRepository;
import com.athletica.backend.repository.UserRepository;
import com.athletica.backend.service.AddressService;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;
  private final CityRepository cityRepository;
  private final UserRepository userRepository;

  public AddressServiceImpl(AddressRepository addressRepository,
      CityRepository cityRepository,
      UserRepository userRepository) {
    this.addressRepository = addressRepository;
    this.cityRepository = cityRepository;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<AddressDto> getAddressesForUser(String userId) {
    // valida existencia de usuario (opcional, pero recomendable)
    if (userId == null || userId.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId es requerido");
    }
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    return addressRepository.findAllByUserId(user.getId())
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public AddressDto getAddressById(String id) {
    return addressRepository.findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada"));
  }

  @Override
  public AddressDto createAddress(AddressRequest request) {
    if (request.getUserId() == null || request.getUserId().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId es requerido");
    }
    if (request.getCityId() == null || request.getCityId().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cityId es requerido");
    }

    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

    City city = cityRepository.findById(request.getCityId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ciudad no encontrada"));

    Address a = new Address();
    a.setId(UUID.randomUUID().toString());
    a.setUser(user);
    a.setLabel(request.getLabel());
    a.setAddressLine1(request.getAddressLine1());
    a.setAddressLine2(request.getAddressLine2());
    a.setCity(city);
    a.setState(request.getState());
    a.setPostalCode(request.getPostalCode());
    a.setPhone(request.getPhone());
    a.setCreatedAt(LocalDateTime.now());

    Address saved = addressRepository.save(a);
    return toDto(saved);
  }

  @Override
  public AddressDto updateAddress(String id, AddressRequest request) {
    return addressRepository.findById(id).map(existing -> {
      if (request.getCityId() != null && !request.getCityId().isBlank()) {
        City city = cityRepository.findById(request.getCityId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ciudad no encontrada"));
        existing.setCity(city);
      }

      // Actualizamos solo si vienen valores (patch-like)
      if (request.getLabel() != null)
        existing.setLabel(request.getLabel());
      if (request.getAddressLine1() != null)
        existing.setAddressLine1(request.getAddressLine1());
      if (request.getAddressLine2() != null)
        existing.setAddressLine2(request.getAddressLine2());
      if (request.getState() != null)
        existing.setState(request.getState());
      if (request.getPostalCode() != null)
        existing.setPostalCode(request.getPostalCode());
      if (request.getPhone() != null)
        existing.setPhone(request.getPhone());

      Address saved = addressRepository.save(existing);
      return toDto(saved);
    }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada"));
  }

  @Override
  public void deleteAddress(String id) {
    if (!addressRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada");
    }
    addressRepository.deleteById(id);
  }

  /* ------------------ Helpers ------------------ */
  private AddressDto toDto(Address a) {
    if (a == null)
      return null;
    AddressDto dto = new AddressDto();
    dto.setId(a.getId());
    dto.setUserId(a.getUser() != null ? a.getUser().getId() : null);
    dto.setLabel(a.getLabel());
    dto.setAddressLine1(a.getAddressLine1());
    dto.setAddressLine2(a.getAddressLine2());
    dto.setCityId(a.getCity() != null ? a.getCity().getId() : null);
    dto.setCityName(a.getCity() != null ? a.getCity().getName() : null);
    dto.setState(a.getState());
    dto.setPostalCode(a.getPostalCode());
    dto.setPhone(a.getPhone());
    dto.setCreatedAt(a.getCreatedAt());
    return dto;
  }
}
