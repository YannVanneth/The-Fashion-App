package thefashion.authservice.domain.dto.profile;


public record ChangPasswordRequestDTO(
    String oldPassword,
    String newPassword
) {
}
