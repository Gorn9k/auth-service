package ODL.web.authservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ODL.web.authservice.dto.RoleDTO;
import ODL.web.authservice.service.user.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Api(tags = "Role controller", description = "Operations on Roles")
public class RoleController {

    RoleService roleService;

    @GetMapping
    @ApiOperation(value = "Get all available roles for simple user registration")
    public List<RoleDTO> getAvailableRoles() {
        return roleService.getAvailableRoles();
    }
}
