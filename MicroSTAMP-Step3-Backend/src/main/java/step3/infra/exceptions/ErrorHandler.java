package step3.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    // Errors -----------------------------------------

    // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error400Dto>> error400(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(Error400Dto::new).toList());
    }

//    // 404
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Void> error404() {
//        return ResponseEntity.notFound().build();
//    }

    // 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> error404(EntityNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    // 405
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ErrorMessage> error403(OperationNotAllowedException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(errorMessage);
    }

    // DTOs -------------------------------------------

    public record Error400Dto(String field, String message) {
        public Error400Dto(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    public record ErrorMessage(String message) {

    }

    // ------------------------------------------------
}