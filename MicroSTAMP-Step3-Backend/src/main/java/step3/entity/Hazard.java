package step3.entity;

import lombok.*;
import jakarta.persistence.*;

@Table(name = "hazard")
@Entity(name = "Hazard")
@Getter @Setter @NoArgsConstructor
public class Hazard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne @JoinColumn(name = "project_id")
    private Project project;

    private int tag;

    // Constructors -----------------------------------

    public Hazard(String name, Project project) {
        this.name = name;
        this.project = project;
        this.tag = -1;
    }

    // Methods ----------------------------------------

    public String getTagName() {
        return "H" + this.tag;
    }

    // ------------------------------------------------
}
