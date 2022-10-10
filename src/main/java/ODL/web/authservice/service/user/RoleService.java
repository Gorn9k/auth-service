package ODL.web.authservice.service.user;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ODL.web.authservice.dto.RoleDTO;
import ODL.web.authservice.entity.user.Role;
import ODL.web.authservice.exception.BusinessEntityNotFoundException;
import ODL.web.authservice.exception.BusinessException;
import ODL.web.authservice.repository.RoleRepository;
import ODL.web.authservice.service.CrudImpl;
import ODL.web.authservice.service.mapper.RoleDTOMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Role service
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService extends CrudImpl<Role, RoleRepository, RoleDTO, RoleDTOMapper> {

    RoleDTOMapper roleDTOMapper;

    /**
     * Get all roles with {@link Role#freeAccess} = true (converted into
     * {@link RoleDTO})
     * 
     * @return {@link RoleDTO} array
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> getAvailableRoles() {
        return roleDTOMapper.toDTOs(repository.findByFreeAccessTrue());
    }

    /**
     * Get {@link Role} by name
     * 
     * @param name Role name
     * @return {@link Role}
     * @throws BusinessEntityNotFoundException if role not found
     */
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new BusinessEntityNotFoundException("Role not found"));
    }

    /**
     * Create new Role
     */
    @Override
    @Transactional
    public RoleDTO create(RoleDTO roleDTO) {
        if (repository.existsByNameIgnoreCase(roleDTO.getName())) {
            throw new BusinessException("Role name should be unique");
        }
        Role role = roleDTOMapper.toEntity(roleDTO);
        return roleDTOMapper.toDTO(repository.save(role));
    }

    /**
     * Update existing {@link Role}
     * 
     * @param roleId  existing {@link Role#id}
     * @param roleDTO {@link RoleDTO} with updates
     * @throws BusinessException if new {@link Role#name} is no uniq
     * @return updated {@link Role} (converted into {@link RoleDTO})
     */
    @Transactional
    public RoleDTO update(UUID roleId, RoleDTO roleDTO) {
        Role role = get(roleId);
        if (repository.existsByNameAndIdNot(roleDTO.getName(), roleId)) {
            throw new BusinessException("Role name should be unique");
        }
        role.setName(roleDTO.getName());
        role.setDisplayName(roleDTO.getDisplayName());
        return roleDTOMapper.toDTO(repository.save(role));
    }
}
