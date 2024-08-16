package step3.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "rule")
@Entity(name = "Rule")
@Getter @Setter @NoArgsConstructor
public class Rule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int tag;

    @ManyToOne @JoinColumn(name = "control_action_id")
    private ControlAction controlAction;

    @ManyToMany
    @JoinTable(
        name = "rule_value",
        joinColumns = @JoinColumn(name = "rule_id"),
        inverseJoinColumns = @JoinColumn(name = "value_id")
    )
    private List<Value> values = new ArrayList<>();

    @ManyToOne @JoinColumn(name = "hazard_id")
    private Hazard hazard;

    @ElementCollection(targetClass = UCAType.class)
    @CollectionTable(name = "rule_type", joinColumns = @JoinColumn(name = "rule_id"))
    @Enumerated(EnumType.STRING) @Column(name = "type")
    private Set<UCAType> types = new HashSet<>();

    // Constructors -----------------------------------

    public Rule(String name, ControlAction controlAction, List<Value> values, Hazard hazard, Set<UCAType> types) {
        this.name = name;
        this.controlAction = controlAction;
        this.values = values;
        this.hazard = hazard;
        this.types = types;
    }

    // Methods ----------------------------------------

    public void addType(UCAType type) {
        types.add(type);
    }

    public String getTagName() {
        return "R" + this.tag;
    }

    // ------------------------------------------------
}
