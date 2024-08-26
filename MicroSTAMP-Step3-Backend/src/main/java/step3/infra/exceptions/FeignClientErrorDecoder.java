package step3.infra.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import step3.infra.exceptions.response.feignclient.FeignErrorResponse;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
//        log.info("Error Decoder: {}, {}", methodKey, response);
        Response.Body responseBody = response.body();
        FeignErrorResponse message = null;

        try(InputStream bodyIs =  responseBody.asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, FeignErrorResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        String messageError = message.errors().get(0).message();

        if (response.status() == 404) {
            throw new EntityNotFoundException(messageError != null ? messageError : "Resource not found.");
        }

        throw new RuntimeException(messageError != null ? messageError : "Unexpected Error.");

    }
}
