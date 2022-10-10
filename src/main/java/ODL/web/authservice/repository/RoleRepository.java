package ODL.web.authservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ODL.web.authservice.entity.user.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    List<Role> findByFreeAccessTrue();

    Optional<Role> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameAndIdNot(String name, UUID roleId);

}
