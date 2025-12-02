package thefashion.authservice.service.abstraction;

import thefashion.authservice.domain.dto.authentication.AuthResponseDTO;
import thefashion.authservice.domain.dto.authentication.LoginRequestDTO;
import thefashion.authservice.domain.dto.authentication.LoginResponseDTO;
import thefashion.authservice.domain.dto.authentication.RegisterRequestDTO;

public interface AuthServiceAbstract {
    AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
