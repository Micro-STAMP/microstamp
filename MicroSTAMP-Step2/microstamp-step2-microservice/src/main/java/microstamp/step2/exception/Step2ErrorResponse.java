package microstamp.step2.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Step2ErrorResponse {

    private List<Step2Error> errors = new ArrayList<>();

    public void addError(Step2Error error){
        errors.add(error);
    }
}
