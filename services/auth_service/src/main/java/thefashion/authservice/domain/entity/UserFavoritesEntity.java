package thefashion.authservice.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "user_favorites" , uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "user_id",
                "product_id"
        })
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoritesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
