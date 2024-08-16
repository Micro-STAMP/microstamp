package microstamp.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MicroStampError {

    private String type;

    private String key;

    private String message;
}
