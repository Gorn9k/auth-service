package ODL.web.authservice.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ODL.web.authservice.entity.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, String> {

    Optional<UserToken> getByTokenAndExpiredAfter(String token, LocalDateTime date);

    Optional<UserToken> getByUserEmail(String email);
}
