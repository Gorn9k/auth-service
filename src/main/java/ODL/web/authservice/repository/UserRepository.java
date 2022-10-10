package ODL.web.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ODL.web.authservice.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByEmailAndIdNotAndDeletedFalse(String email, UUID id);

    @Modifying
    @Query(value = "UPDATE User u SET u.activated = true where u.email = :email")
    void confirmUserEmail(String email);

}
