package microstamp.step4new.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Step4NewIllegalArgumentException extends RuntimeException {

    public Step4NewIllegalArgumentException(String message) {
        super(message);
    }
}