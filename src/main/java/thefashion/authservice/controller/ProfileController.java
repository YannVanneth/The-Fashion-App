package thefashion.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import thefashion.authservice.domain.dto.profile.ChangPasswordRequestDTO;
import thefashion.authservice.domain.dto.profile.UpdateEmailRequestDTO;
import thefashion.authservice.domain.dto.profile.UpdateProfileRequestDTO;
import thefashion.authservice.domain.dto.profile.UserProfileResponseDTO;
import thefashion.authservice.service.abstraction.ProfileServiceAbstract;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceAbstract profileService;


    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> getMyProfile(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        UserProfileResponseDTO profile = profileService.getMyProfile(userId);
        return ResponseEntity.ok(profile);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(
            @PathVariable String userId,
            Authentication authentication) {
        UserProfileResponseDTO profile = profileService.getMyProfile(userId);
        return ResponseEntity.ok(profile);
    }


    @PutMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> updateMyProfile(
            @RequestBody UpdateProfileRequestDTO updateProfileRequestDTO,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        UserProfileResponseDTO profile = profileService.updateMyProfile(userId, updateProfileRequestDTO);
        return ResponseEntity.ok(profile);
    }

    /**
     * Change password for authenticated user
     */
    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangPasswordRequestDTO changPasswordRequestDTO,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        profileService.changePassword(userId, changPasswordRequestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Update email for authenticated user
     */
    @PutMapping("/me/email")
    public ResponseEntity<Void> updateEmail(
            @RequestBody UpdateEmailRequestDTO updateEmailRequestDTO,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        profileService.updateEmail(userId, updateEmailRequestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Verify email change with verification code
     */
    @PostMapping("/me/email/verify")
    public ResponseEntity<Void> verifyEmailChange(
            @RequestParam String verificationCode,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        profileService.verifyEmailChange(userId, verificationCode);
        return ResponseEntity.ok().build();
    }
}

