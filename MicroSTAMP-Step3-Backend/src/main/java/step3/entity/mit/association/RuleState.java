package step3.entity.mit.association;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "RuleState")
@Table(name = "rule_state")
public class RuleState {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID ruleId;
    private UUID stateId;
}
