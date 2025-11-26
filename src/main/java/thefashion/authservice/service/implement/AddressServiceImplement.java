package thefashion.authservice.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thefashion.authservice.domain.Role;
import thefashion.authservice.domain.dto.address.AddressRequestDTO;
import thefashion.authservice.domain.dto.address.AddressResponseDTO;
import thefashion.authservice.domain.entity.UserAddressesEntity;
import thefashion.authservice.domain.entity.UserEntity;
import thefashion.authservice.mapper.AddressMapper;
import thefashion.authservice.repository.AddressRepository;
import thefashion.authservice.repository.UserRepository;
import thefashion.authservice.service.abstraction.AddressServiceAbstract;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImplement implements AddressServiceAbstract {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    public List<AddressResponseDTO> getAllAddresses(String userId) {
        validateUserAccess(userId);

        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addressRepository.findByUser(userEntity).stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponseDTO getAddressById(String userId, String addressId) {
        validateUserAccess(userId);

        UserAddressesEntity address = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("You can only access your own addresses");
        }

        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional
    public AddressResponseDTO createAddress(String userId, AddressRequestDTO addressRequestDTO) {
        validateUserAccess(userId);

        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate unique addressId
        String addressId = UUID.randomUUID().toString();

        UserAddressesEntity address = new UserAddressesEntity();
        address.setUser(user);
        address.setAddressId(addressId);
        address.setAddressLine1(addressRequestDTO.addressLine1());
        address.setAddressLine2(addressRequestDTO.addressLine2());
        address.setCity(addressRequestDTO.city());
        address.setCountry(addressRequestDTO.country());
        address.setPostalCode(addressRequestDTO.postalCode());
        address.setCreatedAt(LocalDateTime.now());

        // Check if this is the first address for the user
        List<UserAddressesEntity> existingAddresses = addressRepository.findByUser(user);
        if (existingAddresses.isEmpty()) {
            // First address is automatically default
            address.setDefault(true);
        } else {
            address.setDefault(addressRequestDTO.isDefault());

            // If this address is set as default, unset others
            if (addressRequestDTO.isDefault()) {
                existingAddresses.forEach(addr -> {
                    addr.setDefault(false);
                    addressRepository.save(addr);
                });
            }
        }

        UserAddressesEntity savedAddress = addressRepository.save(address);
        return addressMapper.toResponse(savedAddress);
    }

    @Override
    @Transactional
    public AddressResponseDTO updateAddress(String userId, String addressId, AddressRequestDTO addressRequestDTO) {
        validateUserAccess(userId);

        UserAddressesEntity address = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("You can only update your own addresses");
        }

        if (addressRequestDTO.addressLine1() != null && !addressRequestDTO.addressLine1().isBlank()) {
            address.setAddressLine1(addressRequestDTO.addressLine1());
        }
        if (addressRequestDTO.addressLine2() != null) {
            address.setAddressLine2(addressRequestDTO.addressLine2());
        }
        if (addressRequestDTO.city() != null && !addressRequestDTO.city().isBlank()) {
            address.setCity(addressRequestDTO.city());
        }
        if (addressRequestDTO.country() != null && !addressRequestDTO.country().isBlank()) {
            address.setCountry(addressRequestDTO.country());
        }
        if (addressRequestDTO.postalCode() != null && !addressRequestDTO.postalCode().isBlank()) {
            address.setPostalCode(addressRequestDTO.postalCode());
        }

        // Handle default status
        if (addressRequestDTO.isDefault() && !address.isDefault()) {
            // Unset other default addresses
            UserEntity user = address.getUser();
            addressRepository.findByUser(user).forEach(addr -> {
                if (!addr.getAddressId().equals(addressId)) {
                    addr.setDefault(false);
                    addressRepository.save(addr);
                }
            });
            address.setDefault(true);
        }

        UserAddressesEntity updatedAddress = addressRepository.save(address);
        return addressMapper.toResponse(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(String userId, String addressId) {
        validateUserAccess(userId);

        UserAddressesEntity address = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("You can only delete your own addresses");
        }

        boolean wasDefault = address.isDefault();
        UserEntity user = address.getUser();

        addressRepository.delete(address);

        // If deleted address was default, set another address as default
        if (wasDefault) {
            List<UserAddressesEntity> remainingAddresses = addressRepository.findByUser(user);
            if (!remainingAddresses.isEmpty()) {
                UserAddressesEntity newDefault = remainingAddresses.getFirst();
                newDefault.setDefault(true);
                addressRepository.save(newDefault);
            }
        }
    }

    @Override
    @Transactional
    public AddressResponseDTO setDefaultAddress(String userId, String addressId) {
        validateUserAccess(userId);

        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserAddressesEntity newDefaultAddress = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!newDefaultAddress.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("You can only set your own addresses as default");
        }

        // Unset all other default addresses
        addressRepository.findByUser(user).forEach(address -> {
            address.setDefault(false);
            addressRepository.save(address);
        });

        // Set new default
        newDefaultAddress.setDefault(true);
        UserAddressesEntity savedAddress = addressRepository.save(newDefaultAddress);

        return addressMapper.toResponse(savedAddress);
    }

    private void validateUserAccess(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String authenticatedUserId = (String) authentication.getPrincipal();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(authority -> authority.replace("ROLE_", ""))
                .orElse("CUSTOMER");

        if (!Role.ADMIN.name().equals(role) && !authenticatedUserId.equals(userId)) {
            throw new AccessDeniedException("You can only access your own addresses");
        }
    }
}

