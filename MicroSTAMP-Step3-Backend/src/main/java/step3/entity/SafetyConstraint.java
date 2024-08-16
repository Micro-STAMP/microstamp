package step3.entity;

import lombok.*;
import jakarta.persistence.*;

@Table(name = "safety_constraint")
@Entity(name = "SafetyConstraint")
@Getter @Setter @NoArgsConstructor
public class SafetyConstraint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne @JoinColumn(name = "uca_id")
    UnsafeControlAction unsafeControlAction;

    // Constructors -----------------------------------

    public SafetyConstraint(String name) {
        this.name = name;
    }

    public SafetyConstraint(String name, UnsafeControlAction uca) {
        this.name = name;
        this.unsafeControlAction = uca;
    }

    // Methods ----------------------------------------

    // ------------------------------------------------
}
