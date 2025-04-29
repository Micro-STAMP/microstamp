package microstamp.step4.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Step4IllegalArgumentException extends RuntimeException {

    public Step4IllegalArgumentException(String message) {
        super(message);
    }
}
