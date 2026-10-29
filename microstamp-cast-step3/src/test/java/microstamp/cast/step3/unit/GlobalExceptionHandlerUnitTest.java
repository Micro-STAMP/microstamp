package microstamp.cast.step3.unit;

import microstamp.cast.step3.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerUnitTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("#handleDataIntegrityViolation > When the code is duplicated > Return 409 with a friendly message")
    void handleDataIntegrityViolationWhenTheCodeIsDuplicatedReturnConflict() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("could not execute statement",
                new SQLIntegrityConstraintViolationException("Duplicate entry 'CAC-1-123' for key 'control_action_contributions.UK_code_analysis'"));

        ResponseEntity<Map<String, Object>> response = handler.handleDataIntegrityViolation(ex);

        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode()),
                () -> assertEquals("This code is already in use in this analysis.", response.getBody().get("message"))
        );
    }
}
