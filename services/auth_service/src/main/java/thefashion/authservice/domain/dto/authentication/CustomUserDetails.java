package thefashion.authservice.domain.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import thefashion.authservice.domain.entity.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private UUID userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;

    public CustomUserDetails(UserEntity userEntity) {
        this.userId = UUID.fromString(userEntity.getUserId());
        this.email = userEntity.getEmail();
        this.password = userEntity.getPasswordHash();
        this.firstName = userEntity.getFistName(); // Note: there's a typo in the entity field name
        this.lastName = userEntity.getLastName();
        this.role = userEntity.getRole().name();
        this.isEnabled = "ACTIVE".equals(userEntity.getStatus());
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Using email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    // Additional helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
