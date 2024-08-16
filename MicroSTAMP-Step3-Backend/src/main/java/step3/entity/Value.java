package step3.entity;

import lombok.*;
import jakarta.persistence.*;

@Table(name = "value")
@Entity(name = "Value")
@Getter @Setter @NoArgsConstructor
public class Value {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne @JoinColumn(name = "variable_id")
    private Variable variable;

    // Constructors -----------------------------------

    public Value(String name, Variable variable) {
        this.name = name;
        this.variable = variable;
    }

    // Methods ----------------------------------------

    @Override
    public String toString() {
        return "{ " + variable.getName() + " = " + getName() + " }";
    }


    // ------------------------------------------------
}
