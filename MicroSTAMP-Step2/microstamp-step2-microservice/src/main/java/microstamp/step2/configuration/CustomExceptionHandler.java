package microstamp.step2.configuration;

import feign.FeignException;
import microstamp.step2.enumeration.ComponentType;
import microstamp.step2.enumeration.ConnectionActionType;
import microstamp.step2.enumeration.Style;
import microstamp.step2.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Step2ErrorResponse errorResponse = new Step2ErrorResponse();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage;
            String fieldName = ((FieldError) error).getField();

            if(fieldName.equals("nameEqualsEnvironment")) {
                fieldName = "name";
                errorMessage = error.getDefaultMessage();
            }
            else
                errorMessage = fieldName + " is mandatory";

            Step2Error step2Error = new Step2Error(ex.getClass().getSimpleName(), fieldName, errorMessage);
            errorResponse.addError(step2Error);
        });

        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        Step2ErrorResponse errorResponse = new Step2ErrorResponse();
        Throwable cause = ex.getCause();

        if (cause.getMessage().contains(ConnectionActionType.class.getSimpleName())) {
            errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(), "InvalidEnumValue", "Invalid enum value, valid fields: " + Arrays.toString(ConnectionActionType.values())));
        } else if (cause.getMessage().contains(Style.class.getSimpleName())){
            errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(), "InvalidEnumValue", "Invalid enum value, valid fields: " + Arrays.toString(Style.values())));
        } else if (cause.getMessage().contains(ComponentType.class.getSimpleName())){
            errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(), "InvalidEnumValue", "Invalid enum value, valid fields: " + Arrays.toString(ComponentType.values())));
        } else {
            errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(), "HttpMessageNotReadableException",ex.getMessage()));
        }

        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { FeignException.class })
    protected ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest request) {
        Step2ErrorResponse errorResponse = new Step2ErrorResponse();
        HttpStatus httpStatus = HttpStatus.valueOf(ex.status());
        errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(),httpStatus.name(),ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler(value = { SQLIntegrityConstraintViolationException.class })
    protected ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        Step2ErrorResponse errorResponse = new Step2ErrorResponse();
        if(ex.getMessage().contains("Duplicate entry"))
            errorResponse.addError(new Step2Error("Step2DuplicatedCodeException","DuplicatedCode", "Two artifacts of the same type cannot have the same code"));
        else
            errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(),"SQLIntegrityConstraintViolation",ex.getCause().getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { Step2NotFoundException.class })
    protected ResponseEntity<Object> handleStep2NotFound(Step2NotFoundException ex, WebRequest request) {
        Step2ErrorResponse errorResponse = new Step2ErrorResponse();
        errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(),"NotFound",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { Step2OrphanException.class })
    protected ResponseEntity<Object> handleStep2Orphan(Step2OrphanException ex, WebRequest request) {
        Step2ErrorResponse errorResponse = new Step2ErrorResponse();
        errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(),"OrphanException",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { Step2InvalidConnectionActionException.class })
    protected ResponseEntity<Object> handleStep2InvalidConnection(Step2InvalidConnectionActionException ex, WebRequest request) {
        Step2ErrorResponse errorResponse = new Step2ErrorResponse();
        errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(),"InvalidConnection",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { Step2SelfParentingComponentException.class })
    protected ResponseEntity<Object> handleStep2SelfParentingComponentException(Step2SelfParentingComponentException ex, WebRequest request) {
        Step2ErrorResponse errorResponse = new Step2ErrorResponse();
        errorResponse.addError(new Step2Error(ex.getClass().getSimpleName(),"SelfParentingComponentException",ex.getMessage()));
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
