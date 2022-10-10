package ODL.web.authservice.dto.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel
public class UserDTO extends AccountDTO {

    @NotNull
    @ApiModelProperty(hidden = true)
    Boolean locked;

    @NotNull
    @ApiModelProperty(hidden = true)
    Boolean activated;

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true)
    Boolean deleted;
}
