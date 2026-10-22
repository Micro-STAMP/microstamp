package microstamp.cast.step5.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.cast.step5.entity.enums.RecommendationPriority;
import microstamp.cast.step5.entity.enums.RecommendationScopeCategory;
import microstamp.cast.step5.entity.enums.RecommendationStatus;
import microstamp.cast.step5.entity.enums.RecommendationType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "analysis_id", nullable = false)
    private UUID analysisId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "recommendation_control_action_contributions", joinColumns = @JoinColumn(name = "recommendation_id"))
    @Column(name = "control_action_contribution_id")
    private List<UUID> controlActionContributionIds;

    @ElementCollection
    @CollectionTable(name = "recommendation_systemic_factors", joinColumns = @JoinColumn(name = "recommendation_id"))
    @Column(name = "systemic_factor_id")
    private List<UUID> systemicFactorIds;

    @Column(name = "component_id")
    private UUID componentId;

    @Enumerated(EnumType.STRING)
    private RecommendationPriority priority;

    @Column(name = "audit_mechanism", columnDefinition = "TEXT")
    private String auditMechanism;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RecommendationStatus status = RecommendationStatus.PROPOSED;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_type")
    private RecommendationType recommendationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_category")
    private RecommendationScopeCategory scopeCategory;

    @Column(name = "effectiveness_evidence", columnDefinition = "TEXT")
    private String effectivenessEvidence;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
