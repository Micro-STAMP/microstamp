package microstamp.cast.step4.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.cast.step4.entity.enums.SystemicFactorCategory;

import java.util.List;
import java.util.UUID;

@Entity
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
    @Column(nullable = false)
    private SystemicFactorCategory category;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "systemic_factor_inadequate_control_actions", joinColumns = @JoinColumn(name = "systemic_factor_id"))
    @Column(name = "inadequate_control_action_id")
    private List<UUID> inadequateControlActionIds;
}
