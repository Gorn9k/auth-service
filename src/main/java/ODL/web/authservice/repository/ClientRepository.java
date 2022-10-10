package ODL.web.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ODL.web.authservice.entity.user.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByClientId(String clientId);

}
