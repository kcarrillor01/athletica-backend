package com.athletica.backend.service;

import java.util.List;

import com.athletica.backend.dto.AddressDto;
import com.athletica.backend.dto.AddressRequest;

public interface AddressService {
  
  List<AddressDto> getAddressesForUser(String userId);

  AddressDto getAddressById(String id);

  AddressDto createAddress(AddressRequest request);

  AddressDto updateAddress(String id, AddressRequest request);

  void deleteAddress(String id);
}
