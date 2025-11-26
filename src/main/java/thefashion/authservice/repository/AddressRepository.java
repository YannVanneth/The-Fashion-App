package thefashion.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thefashion.authservice.domain.entity.UserAddressesEntity;
import thefashion.authservice.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<UserAddressesEntity, Long> {

    List<UserAddressesEntity> findByUser(UserEntity user);



    Optional<UserAddressesEntity> findByAddressId(String addressId);


}
