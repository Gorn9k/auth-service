package ODL.web.authservice.controller.admin;

import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ODL.web.authservice.dto.user.UserDTO;
import ODL.web.authservice.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Api(tags = "Admin user controller", description = "Operations on users (admins only)")
public class AdminUserController {

    UserService userService;

    @GetMapping
    @ApiOperation(value = "Get all users")
    public Page<UserDTO> getUsers(@ApiIgnore Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @PostMapping
    @ApiOperation(value = "Create new user")
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createAccount(userDTO);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update existing user")
    public UserDTO patchUser(@PathVariable @ApiParam(example = "User id for editing", required = true) UUID id,
            @RequestBody @ApiParam(example = "{\n \"email\":\"newEmail@email.com\"\n}", required = true) Map<String, Object> patchMap) {
        return userService.patchUser(id, patchMap);
    }
}
