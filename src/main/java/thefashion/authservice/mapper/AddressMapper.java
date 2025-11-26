package thefashion.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thefashion.authservice.domain.dto.address.AddressResponseDTO;
import thefashion.authservice.domain.entity.UserAddressesEntity;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(source = "addressId", target = "addressId")
    @Mapping(source = "addressLine1", target = "addressLine1")
    @Mapping(source = "addressLine2", target = "addressLine2")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "default", target = "isDefault")
    AddressResponseDTO toResponse(UserAddressesEntity entity);
}
