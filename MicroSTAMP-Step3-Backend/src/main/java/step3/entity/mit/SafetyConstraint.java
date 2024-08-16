package step3.entity.mit;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "safety_constraint")
@Entity(name = "SafetyConstraint")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafetyConstraint {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @OneToOne @JoinColumn(name = "uca_id")
    UnsafeControlAction unsafeControlAction;

    //acho que pode remover esses construtores
    public SafetyConstraint(String name) {
        this.name = name;
    }

    public SafetyConstraint(String name, UnsafeControlAction uca) {
        this.name = name;
        this.unsafeControlAction = uca;
    }
}
