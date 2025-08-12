package microstamp.step4new.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Step4NewError {

    private String type;

    private String key;

    private String message;
}