package thefashion.authservice.service.abstraction;

import thefashion.authservice.domain.dto.address.AddressRequestDTO;
import thefashion.authservice.domain.dto.address.AddressResponseDTO;

import java.util.List;

public interface AddressServiceAbstract {

    // Get all addresses of a user
    List<AddressResponseDTO> getAllAddresses(String userId);

    // Get a single address by ID
    AddressResponseDTO getAddressById(String userId, String addressId);

    // Create a new address
    AddressResponseDTO createAddress(String userId, AddressRequestDTO addressRequestDTO);

    // Update an existing address
    AddressResponseDTO updateAddress(String userId, String addressId, AddressRequestDTO addressRequestDTO);

    // Delete an address
    void deleteAddress(String userId, String addressId);

    // Set default address
    AddressResponseDTO setDefaultAddress(String userId, String addressId);
}
