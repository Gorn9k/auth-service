package ODL.web.authservice.service.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import ODL.web.authservice.dto.user.AccountDTO;
import ODL.web.authservice.entity.user.Role;
import ODL.web.authservice.entity.user.User;
import ODL.web.authservice.service.user.RoleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Account mapper
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountDTOMapper implements EntityToDTOMapper<User, AccountDTO> {

    ModelMapper mapper = new ModelMapper();
    RoleDTOMapper roleMapper;
    RoleService roleService;

    public AccountDTOMapper(RoleDTOMapper roleMapper, RoleService roleService) {
        this.roleMapper = roleMapper;
        this.roleService = roleService;

        mapper.addMappings(new PropertyMap<AccountDTO, User>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                skip().setRoles(null);
            }
        });
        mapper.addMappings(new PropertyMap<User, AccountDTO>() {
            @Override
            protected void configure() {
                skip().setRoles(null);
            }
        });
    }

    /**
     * Convert {@link User} to {@link AccountDTO}
     */
    @Override
    public AccountDTO toDTO(User entity, Object... args) {
        AccountDTO doo = mapper.map(entity, AccountDTO.class);
        doo.setRoles(roleMapper.toDTOs(new ArrayList<Role>(entity.getRoles())));
        return doo;
    }

    /**
     * Convert {@link AccountDTO} to {@link User}
     */
    @Override
    public User toEntity(AccountDTO dto, Object... args) {
        User entity = mapper.map(dto, User.class);
        entity.setRoles(dto.getRoles().stream().map(role -> roleService.get(role.getId())).collect(Collectors.toSet()));
        return entity;
    }
}
