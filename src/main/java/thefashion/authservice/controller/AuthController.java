package thefashion.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thefashion.authservice.domain.dto.authentication.AuthResponseDTO;
import thefashion.authservice.domain.dto.authentication.LoginRequestDTO;
import thefashion.authservice.domain.dto.authentication.LoginResponseDTO;
import thefashion.authservice.domain.dto.authentication.RegisterRequestDTO;
import thefashion.authservice.service.implement.AuthServiveImplement;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiveImplement authService;

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

}
