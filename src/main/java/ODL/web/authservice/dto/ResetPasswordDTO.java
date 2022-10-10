package ODL.web.authservice.dto;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel
public class ResetPasswordDTO {

    @NotEmpty
    @ApiModelProperty(example = "Some token", required = true, notes = "Get on email from /drop-password")
    String token;

    @NotEmpty
    @ApiModelProperty(example = "NewPassword", required = true, notes = "New Password")
    String password;
}
