package microstamp.cast.step1.dto.auditlog;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntryReadDto {

    private UUID id;

    private String step;

    private String fieldName;

    private Instant timestamp;
}
