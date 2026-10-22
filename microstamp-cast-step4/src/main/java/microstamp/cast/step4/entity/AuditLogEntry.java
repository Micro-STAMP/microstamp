package microstamp.cast.step4.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CastAuditLogEntry")
@Table(name = "cast_audit_log_entries")
public class AuditLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

    private String step;

    @Column(name = "field_name")
    private String fieldName;

    private Instant timestamp;
}
