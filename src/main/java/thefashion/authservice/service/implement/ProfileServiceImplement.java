package thefashion.authservice.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import thefashion.authservice.domain.Role;
import thefashion.authservice.domain.dto.profile.ChangPasswordRequestDTO;
import thefashion.authservice.domain.dto.profile.UpdateEmailRequestDTO;
import thefashion.authservice.domain.dto.profile.UpdateProfileRequestDTO;
import thefashion.authservice.domain.dto.profile.UserProfileResponseDTO;
import thefashion.authservice.domain.entity.UserEntity;
import thefashion.authservice.mapper.MapFromUserEntityToUserResponsedto;
import thefashion.authservice.repository.UserRepository;
import thefashion.authservice.security.PasswordEnd;
import thefashion.authservice.service.abstraction.ProfileServiceAbstract;

@Service
@RequiredArgsConstructor
public class ProfileServiceImplement implements ProfileServiceAbstract {

    private final UserRepository userRepository;
    private final MapFromUserEntityToUserResponsedto mapFromUserEntityToUserResponsedto;
    private final PasswordEnd passwordEnd;

    @Override
    public UserProfileResponseDTO getMyProfile(String userId) {

               Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }


        String authenticatedUserId = (String) authentication.getPrincipal();


        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(authority -> authority.replace("ROLE_", ""))
                .orElse("CUSTOMER");


        if (!Role.ADMIN.name().equals(role) &&
            (authenticatedUserId == null || !authenticatedUserId.equals(userId))) {
            throw new AccessDeniedException("You can only view your own profile");
        }

          UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        return mapFromUserEntityToUserResponsedto.toResponseUserEntity(user);
    }

    @Override
    public UserProfileResponseDTO updateMyProfile(String userId, UpdateProfileRequestDTO updateProfileRequestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authenticatedUserId = (String) authentication.getPrincipal();

        if (authenticatedUserId == null || !authenticatedUserId.equals(userId)) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(authority -> authority.replace("ROLE_", ""))
                .orElse("CUSTOMER");
        if (!Role.ADMIN.name().equals(role) &&
        (authenticatedUserId == null || !authenticatedUserId.equals(userId))) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(updateProfileRequestDTO.getFirstName() != null){
            userEntity.setFistName(updateProfileRequestDTO.getFirstName());
        }

        if(updateProfileRequestDTO.getLastName() != null){
            userEntity.setLastName(updateProfileRequestDTO.getLastName());
        }
        if(updateProfileRequestDTO.getProfilePicture() != null){
            userEntity.setProfilePicture(updateProfileRequestDTO.getProfilePicture());
        }
        UserEntity updatedUser = userRepository.save(userEntity);

        return mapFromUserEntityToUserResponsedto.toResponseUserEntity(updatedUser);
    }

    @Override
    public void changePassword(String userId, ChangPasswordRequestDTO changPasswordRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authenticatedUserId = (String) authentication.getPrincipal();

        if (authenticatedUserId == null || !authenticatedUserId.equals(userId)) {
            throw new AccessDeniedException("You can only change your own password");
        }

        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify old password is correct
        if(!passwordEnd.encoder().matches(changPasswordRequestDTO.oldPassword(), userEntity.getPasswordHash())){
            throw new RuntimeException("Old password is incorrect");
        }

        // Validate new password is not the same as old password
        if(changPasswordRequestDTO.oldPassword().equals(changPasswordRequestDTO.newPassword())){
            throw new RuntimeException("New password cannot be the same as old password");
        }

        userEntity.setPasswordHash(passwordEnd.encoder().encode(changPasswordRequestDTO.newPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public void updateEmail(String userId, UpdateEmailRequestDTO updateEmailRequestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authenticatedUserId = (String) authentication.getPrincipal();

        if (authenticatedUserId == null || !authenticatedUserId.equals(userId)) {
            throw new AccessDeniedException("You can only update your own email");
        }

        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if email is already taken
        if (userRepository.existsByEmail(updateEmailRequestDTO.getNewEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        userEntity.setEmail(updateEmailRequestDTO.getNewEmail());
        userRepository.save(userEntity);
    }

    @Override
    public void verifyEmailChange(String userId, String verificationCode) {
        throw new UnsupportedOperationException("Email verification is not yet implemented");
    }
}
