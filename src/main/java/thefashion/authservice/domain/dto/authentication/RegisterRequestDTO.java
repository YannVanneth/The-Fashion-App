package thefashion.authservice.domain.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role; // Optional: CUSTOMER, VENDOR  (defaults to CUSTOMER if not provided)
}
