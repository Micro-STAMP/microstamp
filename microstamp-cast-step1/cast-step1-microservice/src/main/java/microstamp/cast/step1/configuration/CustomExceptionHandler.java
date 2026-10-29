package microstamp.cast.step1.configuration;

import microstamp.cast.step1.exception.Step1Error;
import microstamp.cast.step1.exception.Step1ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    protected ResponseEntity<Step1ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Step1ErrorResponse errorResponse = new Step1ErrorResponse();
        String cause = ex.getMostSpecificCause().getMessage();
        if (cause != null && cause.contains("Duplicate entry"))
            errorResponse.addError(new Step1Error("Step1DuplicatedCodeException", "DuplicatedCode", "This code is already in use in this analysis."));
        else
            errorResponse.addError(new Step1Error(ex.getClass().getSimpleName(), "DataIntegrityViolation", cause));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
