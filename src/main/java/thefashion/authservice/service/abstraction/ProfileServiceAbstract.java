package thefashion.authservice.service.abstraction;

import thefashion.authservice.domain.dto.profile.ChangPasswordRequestDTO;
import thefashion.authservice.domain.dto.profile.UpdateEmailRequestDTO;
import thefashion.authservice.domain.dto.profile.UpdateProfileRequestDTO;
import thefashion.authservice.domain.dto.profile.UserProfileResponseDTO;

public interface ProfileServiceAbstract {
      UserProfileResponseDTO getMyProfile(String userId);
      UserProfileResponseDTO updateMyProfile(String userId, UpdateProfileRequestDTO updateProfileRequestDTO);
      void   changePassword(String userId , ChangPasswordRequestDTO changPasswordRequestDTO);
      void   updateEmail(String userId , UpdateEmailRequestDTO updateEmailRequestDTO);
      void verifyEmailChange(String userId , String verificationCode);
}
