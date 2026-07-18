package microstamp.cast.step3.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.cast.step3.entity.enums.IcaType;
import java.util.UUID;

@Entity
@Table(name = "inadequate_control_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InadequateControlAction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "analysis_id", nullable = false)
    private UUID analysisId;

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

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(name = "process_model_flaw", columnDefinition = "TEXT")
    private String processModelFlaw;
}