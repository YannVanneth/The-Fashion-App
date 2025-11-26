package thefashion.authservice.domain.dto.address;

public record AddressRequestDTO(
        String addressLine1,
        String addressLine2,
        String city,
        String country,
        String postalCode,
        boolean isDefault
) {
}
