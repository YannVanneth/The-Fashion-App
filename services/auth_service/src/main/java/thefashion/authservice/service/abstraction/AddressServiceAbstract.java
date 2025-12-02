package thefashion.authservice.service.abstraction;

import thefashion.authservice.domain.dto.address.AddressRequestDTO;
import thefashion.authservice.domain.dto.address.AddressResponseDTO;

import java.util.List;

public interface AddressServiceAbstract {
    List<AddressResponseDTO> getAllAddresses(String userId);
    AddressResponseDTO getAddressById(String userId, String addressId);
    AddressResponseDTO createAddress(String userId, AddressRequestDTO addressRequestDTO);
    AddressResponseDTO updateAddress(String userId, String addressId, AddressRequestDTO addressRequestDTO);
    void deleteAddress(String userId, String addressId);
    AddressResponseDTO setDefaultAddress(String userId, String addressId);
}
