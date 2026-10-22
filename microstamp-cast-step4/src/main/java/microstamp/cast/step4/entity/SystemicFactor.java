package microstamp.cast.step4.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.cast.step4.entity.enums.SystemicFactorCategory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "systemic_factors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemicFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "analysis_id", nullable = false)
    private UUID analysisId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SystemicFactorCategory category;

    @Column(name = "custom_category", length = 120)
    private String customCategory;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "systemic_factor_control_action_contributions", joinColumns = @JoinColumn(name = "systemic_factor_id"))
    @Column(name = "control_action_contribution_id")
    private List<UUID> controlActionContributionIds;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
