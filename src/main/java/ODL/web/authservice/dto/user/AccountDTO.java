package ODL.web.authservice.dto.user;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import ODL.web.authservice.dto.RoleDTO;
import ODL.web.authservice.util.CommonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel
public class AccountDTO {

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true)
    UUID id;

    @NotEmpty
    @Email(regexp = CommonUtils.EMAIL_REGEX)
    @ApiModelProperty(example = "example@email.com", required = true)
    String email;

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(required = true)
    List<RoleDTO> roles;

    @NotEmpty
    @Size(min = 4, max = 80)
    @ApiModelProperty(example = "Password1", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

}