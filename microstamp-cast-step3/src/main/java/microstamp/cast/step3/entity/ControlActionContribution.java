package microstamp.cast.step3.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.cast.step3.converter.StringListConverter;
import microstamp.cast.step3.entity.enums.IcaType;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "control_action_contributions", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "code", "analysis_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControlActionContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "analysis_id", nullable = false)
    private UUID analysisId;

    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "component_id", nullable = false)
    private UUID componentId;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(name = "control_action_name", nullable = false)
    private String controlActionName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IcaType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> context;

    @Convert(converter = StringListConverter.class)
    @Column(name = "process_model_flaw", columnDefinition = "TEXT")
    private List<String> processModelFlaw;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> questions;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
