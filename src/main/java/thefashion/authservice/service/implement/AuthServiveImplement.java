package thefashion.authservice.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thefashion.authservice.config.JwtConfig;
import thefashion.authservice.domain.Role;
import thefashion.authservice.domain.dto.authentication.*;
import thefashion.authservice.domain.entity.UserEntity;
import thefashion.authservice.exception.InvalidCredentialsException;
import thefashion.authservice.exception.UserAlreadyExistsException;
import thefashion.authservice.repository.UserRepository;
import thefashion.authservice.security.PasswordEnd;
import thefashion.authservice.service.abstraction.AuthServiceAbstract;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiveImplement implements AuthServiceAbstract {

    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;
    private final PasswordEnd passwordEncoder;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if(userRepository.existsByEmail(registerRequestDTO.getEmail())){
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Determine role: use provided role or default to CUSTOMER
        Role userRole = Role.CUSTOMER; // Default role
        if (registerRequestDTO.getRole() != null && !registerRequestDTO.getRole().isEmpty()) {
            try {
                userRole = Role.valueOf(registerRequestDTO.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role. Allowed roles: CUSTOMER, VENDOR, ADMIN");
            }
        }

        UserEntity user = UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .fistName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail())
                .passwordHash(passwordEncoder.encoder().encode(registerRequestDTO.getPassword()))
                .role(userRole)
                .status("ACTIVE")
                .build();

        UserEntity savedUser = userRepository.save(user);

        // Create CustomUserDetails and generate token using it
        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String token = jwtConfig.generateAccessToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .userId(savedUser.getUserId())
                .firstName(savedUser.getFistName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        CustomUserDetails userDetails = userRepository.findByEmail(loginRequestDTO.getEmail())
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.encoder().matches(loginRequestDTO.getPassword(), userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String accessToken = jwtConfig.generateAccessToken(userDetails);
        String refreshToken = jwtConfig.generateRefreshToken(userDetails);


        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }


}
