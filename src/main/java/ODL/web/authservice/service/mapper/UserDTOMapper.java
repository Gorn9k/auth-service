package ODL.web.authservice.service.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import ODL.web.authservice.dto.user.UserDTO;
import ODL.web.authservice.entity.user.Role;
import ODL.web.authservice.entity.user.User;
import ODL.web.authservice.service.user.RoleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * 
 * User mapper
 *
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDTOMapper implements EntityToDTOMapper<User, UserDTO> {

    RoleService roleService;
    RoleDTOMapper roleMapper;
    ModelMapper mapper = new ModelMapper();

    public UserDTOMapper(RoleService roleService, RoleDTOMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;

        mapper.addMappings(new PropertyMap<UserDTO, User>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                skip().setRoles(null);
                skip().setDeleted(false);
            }
        });
        mapper.addMappings(new PropertyMap<User, UserDTO>() {
            @Override
            protected void configure() {
                skip().setRoles(null);
            }
        });
    }

    /**
     * Convert {@link User} to {@link UserDTO}
     */
    @Override
    public UserDTO toDTO(User entity, Object... args) {
        UserDTO doo = mapper.map(entity, UserDTO.class);
        doo.setRoles(roleMapper.toDTOs(new ArrayList<Role>(entity.getRoles())));
        return doo;
    }

    /**
     * Convert {@link UserDTO} to {@link User}
     */
    @Override
    public User toEntity(UserDTO dto, Object... args) {
        User entity = mapper.map(dto, User.class);
        entity.setRoles(dto.getRoles().stream().map(role -> roleService.get(role.getId())).collect(Collectors.toSet()));
        return entity;
    }
}
