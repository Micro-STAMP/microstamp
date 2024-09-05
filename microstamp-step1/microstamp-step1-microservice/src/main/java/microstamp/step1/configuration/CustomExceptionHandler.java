package microstamp.step1.configuration;

import feign.FeignException;
import microstamp.step1.exception.*;
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
        Step1ErrorResponse errorResponse = new Step1ErrorResponse();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage;
            String fieldName = ((FieldError) error).getField();

            errorMessage = fieldName + " is mandatory";

            Step1Error step2Error = new Step1Error(ex.getClass().getSimpleName(), fieldName, errorMessage);
            errorResponse.addError(step2Error);
        });

        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { FeignException.class })
    protected ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest request) {
        Step1ErrorResponse errorResponse = new Step1ErrorResponse();
        HttpStatus httpStatus = HttpStatus.valueOf(ex.status());
        errorResponse.addError(new Step1Error(ex.getClass().getSimpleName(),httpStatus.name(),ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler(value = { SQLIntegrityConstraintViolationException.class })
    protected ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        Step1ErrorResponse errorResponse = new Step1ErrorResponse();
        if(ex.getMessage().contains("Duplicate entry"))
            errorResponse.addError(new Step1Error("Step1DuplicatedCodeException","DuplicatedCode", "Two artifacts of the same type cannot have the same code"));
        else
            errorResponse.addError(new Step1Error(ex.getClass().getSimpleName(),"SQLIntegrityConstraintViolation",ex.getCause().getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { Step1NotFoundException.class })
    protected ResponseEntity<Object> handleStep1NotFound(Step1NotFoundException ex, WebRequest request) {
        Step1ErrorResponse errorResponse = new Step1ErrorResponse();
        errorResponse.addError(new Step1Error(ex.getClass().getSimpleName(),"NotFound",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { Step1OrphanException.class })
    protected ResponseEntity<Object> handleStep1Orphan(Step1OrphanException ex, WebRequest request) {
        Step1ErrorResponse errorResponse = new Step1ErrorResponse();
        errorResponse.addError(new Step1Error(ex.getClass().getSimpleName(),"OrphanException",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { Step1SelfParentingHazardException.class })
    protected ResponseEntity<Object> handleStep1SelfParentingHazardException(Step1SelfParentingHazardException ex, WebRequest request) {
        Step1ErrorResponse errorResponse = new Step1ErrorResponse();
        errorResponse.addError(new Step1Error(ex.getClass().getSimpleName(),"SelfParentingHazardException",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { Step1IllegalArgumentException.class })
    protected ResponseEntity<Object> handleStep1IllegalArgumentException(Step1IllegalArgumentException ex, WebRequest request) {
        Step1ErrorResponse errorResponse = new Step1ErrorResponse();
        errorResponse.addError(new Step1Error(ex.getClass().getSimpleName(),"IllegalArgumentException",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
