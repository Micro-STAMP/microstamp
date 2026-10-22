package microstamp.cast.step1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PhysicalLossAnalysis")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cast_physical_loss_analyses", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "code", "analysis_id" })
})
public class PhysicalLossAnalysis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    @Column(columnDefinition = "TEXT")
    private String physicalLossDescription;

    @Column(columnDefinition = "TEXT")
    private String affectedEquipment;

    @Column(columnDefinition = "TEXT")
    private String physicalDesignRequirements;

    @Column(columnDefinition = "TEXT")
    private String physicalControls;

    @Column(columnDefinition = "TEXT")
    private String failuresAndUnsafeInteractions;

    @Column(columnDefinition = "TEXT")
    private String missingOrInadequateControls;

    @Column(columnDefinition = "TEXT")
    private String contextualFactors;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}