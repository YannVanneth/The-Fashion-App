package thefashion.authservice.domain.dto.profile;

import lombok.*;
import thefashion.authservice.domain.dto.address.AddressResponseDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfileResponseDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private String role;
    private String status;
    List<AddressResponseDTO> addresses;
}
