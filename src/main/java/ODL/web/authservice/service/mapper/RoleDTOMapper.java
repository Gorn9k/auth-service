package ODL.web.authservice.service.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import ODL.web.authservice.dto.RoleDTO;
import ODL.web.authservice.entity.user.Role;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Role mapper
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleDTOMapper implements EntityToDTOMapper<Role, RoleDTO> {

    ModelMapper mapper = new ModelMapper();

    public RoleDTOMapper() {

        mapper.addMappings(new PropertyMap<RoleDTO, Role>() {
            @Override
            protected void configure() {
                skip(destination.getUserList());
            }
        });
    }

    /**
     * Convert {@link Role} to {@link RoleDTO}
     */
    @Override
    public RoleDTO toDTO(Role entity, Object... args) {
        return mapper.map(entity, RoleDTO.class);
    }

    /**
     * Convert {@link RoleDTO} to {@link Role}
     */
    @Override
    public Role toEntity(RoleDTO dto, Object... args) {
        return mapper.map(dto, Role.class);
    }
}
