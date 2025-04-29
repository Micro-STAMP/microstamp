package microstamp.step4.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Step4ErrorResponse {

    private List<Step4Error> errors = new ArrayList<>();

    public void addError(Step4Error error){
        errors.add(error);
    }
}
