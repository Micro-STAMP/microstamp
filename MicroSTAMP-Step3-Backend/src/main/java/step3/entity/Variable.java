package step3.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "variable")
@Entity(name = "Variable")
@Getter @Setter @NoArgsConstructor
public class Variable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "variable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Value> values = new ArrayList<>();

    @ManyToOne @JoinColumn(name = "controller_id")
    private Controller controller;

    // Constructors -----------------------------------

    public Variable(String name, Controller controller) {
        this.name = name;
        this.controller = controller;
    }

    // Methods ----------------------------------------

    // ------------------------------------------------
}
