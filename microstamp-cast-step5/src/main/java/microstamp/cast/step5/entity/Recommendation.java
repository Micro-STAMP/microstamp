package microstamp.cast.step5.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.cast.step5.entity.enums.RecommendationPriority;

import java.util.List;
import java.util.UUID;

@Entity
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
    @CollectionTable(name = "recommendation_inadequate_control_actions", joinColumns = @JoinColumn(name = "recommendation_id"))
    @Column(name = "inadequate_control_action_id")
    private List<UUID> inadequateControlActionIds;

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
}
