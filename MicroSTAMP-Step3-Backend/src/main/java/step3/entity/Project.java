package step3.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "project")
@Entity(name = "Project")
@Getter @Setter @NoArgsConstructor
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Hazard> hazards = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Controller> controllers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UnsafeControlAction> unsafeControlActions = new ArrayList<>();

    // Constructors -----------------------------------

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Methods ----------------------------------------

    // ------------------------------------------------
}
