package step3.entity.mit;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.entity.mit.association.RuleState;

import java.sql.Types;
import java.util.*;

@Table(name = "rule")
@Entity(name = "Rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rule {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private String name;
    private int code;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID controlActionId;

//    @ManyToMany
//    @JoinTable(
//        name = "rule_value",
//        joinColumns = @JoinColumn(name = "rule_id"),
//        inverseJoinColumns = @JoinColumn(name = "value_id")
//    )
//    private List<step3.entity.Value> values = new ArrayList<>();

    @OneToMany(
            mappedBy = "ruleId",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RuleState> stateAssociations = new ArrayList<>();

    private UUID hazardId;

    @ElementCollection(targetClass = UCAType.class)
    @CollectionTable(name = "rule_type", joinColumns = @JoinColumn(name = "rule_id"))
    @Enumerated(EnumType.STRING) @Column(name = "type")
    private Set<UCAType> types = new HashSet<>();

//    public Rule(String name, ControlAction controlAction, List<Value> values, Hazard hazard, Set<UCAType> types) {
//        this.name = name;
//        this.controlAction = controlAction;
//        this.values = values;
//        this.hazard = hazard;
//        this.types = types;
//    }

    public void addType(UCAType type) {
        types.add(type);
    }

    public String getTagName() {
        return "R" + this.code;
    }

    // ------------------------------------------------
}
