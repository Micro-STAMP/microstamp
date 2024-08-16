package microstamp.step1.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Step1Error {

    private String type;

    private String key;

    private String message;
}
