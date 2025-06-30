package microstamp.step4.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Step4Error {

    private String type;

    private String key;

    private String message;
}
