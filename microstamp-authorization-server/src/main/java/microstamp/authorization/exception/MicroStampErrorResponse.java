package microstamp.authorization.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MicroStampErrorResponse {

    private List<MicroStampError> errors = new ArrayList<>();

    public void addError(MicroStampError error){
        errors.add(error);
    }
}
