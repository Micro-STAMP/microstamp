package step3.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import step3.infra.exceptions.response.MissingField;
import step3.infra.exceptions.response.Step3ExceptionResponse;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Step3ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<MissingField> missingFields = exception.getFieldErrors()
                .stream()
                .map(MissingField::new)
                .toList();

        Step3ExceptionResponse errorMessage = Step3ExceptionResponse.builder()
                .code("400")
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("There are missing fields")
                .missingFields(missingFields)
                .build();

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Step3ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        Step3ExceptionResponse errorMessage = Step3ExceptionResponse.builder()
                .code("400")
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Step3ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        Step3ExceptionResponse errorMessage = Step3ExceptionResponse.builder()
                .code("404")
                .type(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<Step3ExceptionResponse> handleOperationNotAllowedException(OperationNotAllowedException exception) {
        Step3ExceptionResponse errorMessage = Step3ExceptionResponse.builder()
                .code("405")
                .type(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    // 500
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Step3ExceptionResponse> handleException(Exception exception) {
//        Step3ExceptionResponse errorMessage = Step3ExceptionResponse.builder()
//                .code("500")
//                .type(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
//                .message(exception.getMessage())
//                .build();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
//    }

}