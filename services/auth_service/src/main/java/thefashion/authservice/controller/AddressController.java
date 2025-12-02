package thefashion.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import thefashion.authservice.domain.dto.address.AddressRequestDTO;
import thefashion.authservice.domain.dto.address.AddressResponseDTO;
import thefashion.authservice.service.abstraction.AddressServiceAbstract;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressServiceAbstract addressService;


    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        List<AddressResponseDTO> addresses = addressService.getAllAddresses(userId);
        return ResponseEntity.ok(addresses);
    }


    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> getAddressById(
            @PathVariable String addressId,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        AddressResponseDTO address = addressService.getAddressById(userId, addressId);
        return ResponseEntity.ok(address);
    }


    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(
            @RequestBody AddressRequestDTO addressRequestDTO,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        AddressResponseDTO address = addressService.createAddress(userId, addressRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }


    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable String addressId,
            @RequestBody AddressRequestDTO addressRequestDTO,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        AddressResponseDTO address = addressService.updateAddress(userId, addressId, addressRequestDTO);
        return ResponseEntity.ok(address);
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable String addressId,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{addressId}/default")
    public ResponseEntity<AddressResponseDTO> setDefaultAddress(
            @PathVariable String addressId,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        AddressResponseDTO address = addressService.setDefaultAddress(userId, addressId);
        return ResponseEntity.ok(address);
    }
}

