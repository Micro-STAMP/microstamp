package step3.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "controller")
@Entity(name = "Controller")
@Getter @Setter @NoArgsConstructor
public class Controller {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "controller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ControlAction> controlActions = new ArrayList<>();

    @OneToMany(mappedBy = "controller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Variable> variables = new ArrayList<>();

    @OneToOne(mappedBy = "controller", cascade = CascadeType.ALL, orphanRemoval = true)
    private ContextTable contextTable;

    @ManyToOne @JoinColumn(name = "project_id")
    private Project project;

    // Constructors -----------------------------------

    public Controller(String name, Project project) {
        this.name = name;
        this.project = project;
    }

    // Methods ----------------------------------------

    // ------------------------------------------------
}
