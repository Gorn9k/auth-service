package ODL.web.authservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import ODL.web.authservice.util.CommonUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUnconfirmedAccountDTO {

    @NotEmpty
    @Email(regexp = CommonUtils.EMAIL_REGEX)
    String email;

    String role;
}
