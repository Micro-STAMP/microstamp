package microstamp.step4.configuration;

import feign.FeignException;
import microstamp.step4.exception.Step4Error;
import microstamp.step4.exception.Step4ErrorResponse;
import microstamp.step4.exception.Step4IllegalArgumentException;
import microstamp.step4.exception.Step4NotFoundException;
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
        Step4ErrorResponse errorResponse = new Step4ErrorResponse();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage;
            String fieldName = ((FieldError) error).getField();

            errorMessage = fieldName + " is mandatory";

            Step4Error step4Error = new Step4Error(ex.getClass().getSimpleName(), fieldName, errorMessage);
            errorResponse.addError(step4Error);
        });

        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { FeignException.class })
    protected ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest request) {
        Step4ErrorResponse errorResponse = new Step4ErrorResponse();
        HttpStatus httpStatus = HttpStatus.valueOf(ex.status());
        errorResponse.addError(new Step4Error(ex.getClass().getSimpleName(),httpStatus.name(),ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler(value = { SQLIntegrityConstraintViolationException.class })
    protected ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        Step4ErrorResponse errorResponse = new Step4ErrorResponse();
        if(ex.getMessage().contains("Duplicate entry"))
            errorResponse.addError(new Step4Error("Step4DuplicatedCodeException","DuplicatedCode", "Two artifacts of the same type cannot have the same code"));
        else
            errorResponse.addError(new Step4Error(ex.getClass().getSimpleName(),"SQLIntegrityConstraintViolation",ex.getCause().getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { Step4NotFoundException.class })
    protected ResponseEntity<Object> handleStep4NotFound(Step4NotFoundException ex, WebRequest request) {
        Step4ErrorResponse errorResponse = new Step4ErrorResponse();
        errorResponse.addError(new Step4Error(ex.getClass().getSimpleName(),"NotFound",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { Step4IllegalArgumentException.class })
    protected ResponseEntity<Object> handleStep4IllegalArgumentException(Step4IllegalArgumentException ex, WebRequest request) {
        Step4ErrorResponse errorResponse = new Step4ErrorResponse();
        errorResponse.addError(new Step4Error(ex.getClass().getSimpleName(),"IllegalArgumentException",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
