package microstamp.authorization.configuration;

import microstamp.authorization.exception.MicroStampError;
import microstamp.authorization.exception.MicroStampErrorResponse;
import microstamp.authorization.exception.NotFoundException;
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

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        MicroStampErrorResponse errorResponse = new MicroStampErrorResponse();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage;
            String fieldName = ((FieldError) error).getField();

            errorMessage = fieldName + " is mandatory";

            MicroStampError microStampError = new MicroStampError(ex.getClass().getSimpleName(), fieldName, errorMessage);
            errorResponse.addError(microStampError);
        });

        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request) {
        MicroStampErrorResponse errorResponse = new MicroStampErrorResponse();
        errorResponse.addError(new MicroStampError(ex.getClass().getSimpleName(),"NotFound",ex.getMessage()));

        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}
