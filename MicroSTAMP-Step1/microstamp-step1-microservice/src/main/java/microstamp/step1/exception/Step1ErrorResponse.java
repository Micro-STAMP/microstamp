package microstamp.step1.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Step1ErrorResponse {

    private List<Step1Error> errors = new ArrayList<>();

    public void addError(Step1Error error){
        errors.add(error);
    }
}
