package microstamp.cast.step3.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step3.dto.auditlog.AuditLogEntryReadDto;
import microstamp.cast.step3.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - Audit Log")
@RequestMapping("/cast/audit-log")
public class AuditLogController {

    private final AuditLogService service;

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<AuditLogEntryReadDto>> findRecentByAnalysisId(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "20") int limit) {
        log.info("Request received to find recent CAST audit log entries for analysis id {}", id);
        return new ResponseEntity<>(service.findRecentByAnalysisId(id, limit), HttpStatus.OK);
    }
}
