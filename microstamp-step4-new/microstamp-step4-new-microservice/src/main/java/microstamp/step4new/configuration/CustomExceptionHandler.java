package microstamp.step4new.configuration;

import feign.FeignException;
import microstamp.step4new.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Step4NewErrorResponse errorResponse = new Step4NewErrorResponse();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = fieldName + " is mandatory";
            Step4NewError errorItem = new Step4NewError(ex.getClass().getSimpleName(), fieldName, errorMessage);
            errorResponse.addError(errorItem);
        });

        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { FeignException.class })
    protected ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest request) {
        Step4NewErrorResponse errorResponse = new Step4NewErrorResponse();
        HttpStatus httpStatus = HttpStatus.valueOf(ex.status());
        errorResponse.addError(new Step4NewError(ex.getClass().getSimpleName(), httpStatus.name(), ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler(value = { SQLIntegrityConstraintViolationException.class })
    protected ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        Step4NewErrorResponse errorResponse = new Step4NewErrorResponse();
        if(ex.getMessage() != null && ex.getMessage().contains("Duplicate entry"))
            errorResponse.addError(new Step4NewError("Step4NewDuplicatedCodeException","DuplicatedCode", "Two artifacts of the same type cannot have the same code"));
        else
            errorResponse.addError(new Step4NewError(ex.getClass().getSimpleName(),"SQLIntegrityConstraintViolation", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { Step4NewNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(Step4NewNotFoundException ex, WebRequest request) {
        Step4NewErrorResponse errorResponse = new Step4NewErrorResponse();
        errorResponse.addError(new Step4NewError(ex.getClass().getSimpleName(),"NotFound", ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { Step4NewIllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegalArgument(Step4NewIllegalArgumentException ex, WebRequest request) {
        Step4NewErrorResponse errorResponse = new Step4NewErrorResponse();
        errorResponse.addError(new Step4NewError(ex.getClass().getSimpleName(),"IllegalArgumentException", ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}