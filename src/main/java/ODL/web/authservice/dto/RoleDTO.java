package ODL.web.authservice.dto;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel
public class RoleDTO {

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(example = "aaa11a11-1111-11a1-11aa-11111aa1111a", required = true, dataType = "UUID")
    UUID id = UUID.randomUUID();

    @NotEmpty
    @Size(max = 50)
    @ApiModelProperty(example = "USER", required = false)
    String name;

    @NotEmpty
    @Size(max = 50)
    @ApiModelProperty(example = "Пользователь", required = false)
    String displayName;
}
