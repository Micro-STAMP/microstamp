package microstamp.step4new.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Step4NewErrorResponse {

    private List<Step4NewError> errors = new ArrayList<>();

    public void addError(Step4NewError error){
        errors.add(error);
    }
}