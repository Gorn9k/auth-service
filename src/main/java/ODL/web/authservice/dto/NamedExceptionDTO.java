package ODL.web.authservice.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class NamedExceptionDTO {

    LocalDateTime timestamp;

    Integer status;

    String error;

    String message;

    public NamedExceptionDTO(LocalDateTime timestamp, String error, String message) {
        super();
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
    }

}