package microstamp.step2.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Step2Error {

    private String type;

    private String key;

    private String message;
}
