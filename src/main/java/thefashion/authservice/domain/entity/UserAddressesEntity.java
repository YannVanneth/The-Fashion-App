package thefashion.authservice.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String addressId;

    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    private String city;
    private String country;
    private String postalCode;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


}
