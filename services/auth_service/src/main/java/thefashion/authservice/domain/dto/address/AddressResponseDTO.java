package thefashion.authservice.domain.dto.address;

public record AddressResponseDTO(
        String addressId,
        String addressLine1,
        String addressLine2,
        String city,
        String country,
        String postalCode,
        boolean isDefault
) {

}
