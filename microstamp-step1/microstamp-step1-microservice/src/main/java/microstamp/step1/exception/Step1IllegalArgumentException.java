package microstamp.step1.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Step1IllegalArgumentException extends RuntimeException {

    public Step1IllegalArgumentException(String message) {
        super(message);
    }
}
