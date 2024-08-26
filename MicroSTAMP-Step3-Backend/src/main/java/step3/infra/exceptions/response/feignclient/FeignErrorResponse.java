package step3.infra.exceptions.response.feignclient;

import java.util.List;

public record FeignErrorResponse(List<FeignError> errors) {
}

