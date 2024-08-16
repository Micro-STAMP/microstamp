package step3.entity;

import lombok.*;
import jakarta.persistence.*;

@Table(name = "control_action")
@Entity(name = "ControlAction")
@Getter @Setter @NoArgsConstructor
public class ControlAction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne @JoinColumn(name = "controller_id")
    private Controller controller;

    // Constructors -----------------------------------

    public ControlAction(String name, Controller controller) {
        this.name = name;
        this.controller = controller;
    }

    // Methods ----------------------------------------

    // ------------------------------------------------
}
