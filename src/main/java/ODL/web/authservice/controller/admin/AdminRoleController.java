package ODL.web.authservice.controller.admin;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
@RequestMapping("/admin/role")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Api(tags = "Admin role controller", description = "Operations on Roles (admins only)")
public class AdminRoleController {

    RoleService roleService;

    @GetMapping
    @ApiOperation(value = "Get all roles")
    public List<RoleDTO> getRoles() {
        return roleService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new role")
    public RoleDTO createRole(@Valid @RequestBody RoleDTO roleDTO) {
        return roleService.create(roleDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update existing role")
    public RoleDTO updateRole(@PathVariable UUID id, @Valid @RequestBody RoleDTO roleDTO) {
        return roleService.update(id, roleDTO);
    }

}
