package step3.infra.exceptions.response;

import lombok.Builder;

import java.util.List;

@Builder
public record Step3ExceptionResponse(
        String code,
        String type,
        String message,
        List<MissingField> missingFields) {
}
