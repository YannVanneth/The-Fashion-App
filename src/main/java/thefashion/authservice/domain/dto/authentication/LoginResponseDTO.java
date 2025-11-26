package thefashion.authservice.domain.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
