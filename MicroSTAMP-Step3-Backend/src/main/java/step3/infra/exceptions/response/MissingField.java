package step3.infra.exceptions.response;

import org.springframework.validation.FieldError;

public record MissingField(String field, String message) {
    public MissingField(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
