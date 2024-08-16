package step3.entity.mit;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.entity.mit.association.UnsafeControlActionState;
import step3.entity.mit.step2.ControlAction;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Table(name = "unsafe_control_action")
@Entity(name = "UnsafeControlAction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnsafeControlAction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String name;  // NAME : <Source> <Type> <Control Action> <Context>

    @JdbcTypeCode(Types.VARCHAR)
    private UUID controlActionId;

//    @ManyToMany
//    @JoinTable(
//            name = "uca_value",
//            joinColumns = @JoinColumn(name = "uca_id"),
//            inverseJoinColumns = @JoinColumn(name = "value_id")
//    )
    @OneToMany(
            mappedBy = "unsafeControlActionId",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UnsafeControlActionState> stateAssociations = new ArrayList<>();

    @JdbcTypeCode(Types.VARCHAR)
    private UUID hazardId;

    @Enumerated(EnumType.STRING)
    private UCAType type;

    @OneToOne(
            mappedBy = "unsafeControlAction",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private SafetyConstraint constraint;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

    private String ruleCode;

//    public UnsafeControlAction(ControlAction controlAction, List<step3.entity.Value> values, Hazard hazard, UCAType type, Project project, String ruleCode) {
//        this.controlAction = controlAction;
//        this.values = values;
//        this.hazard = hazard;
//        this.type = type;
//        this.project = project;
//        this.name = generateName();
//        this.constraint = generateConstraint();
//        this.ruleCode = ruleCode == null ? "" : ruleCode;
//    }
//    public UnsafeControlAction(ControlAction controlAction, List<step3.entity.Value> values, Hazard hazard, String type, Project project, String ruleCode) {
//        this.controlAction = controlAction;
//        this.values = values;
//        this.hazard = hazard;
//        this.type = UCAType.valueOf(type);
//        this.project = project;
//        this.name = generateName();
//        this.constraint = generateConstraint();
//        this.ruleCode = ruleCode == null ? "" : ruleCode;;
//    }

    // acho que vai precisar migrar isso para as classes de serviço
//    public String generateName() {
//        String source = getControlAction().getController().getName();
//        String typeAndCA = getTypeAndControlActionString();
//        String context = getContextString();
//
//        return source + " " + typeAndCA + " when " + context;
//    }
//    public SafetyConstraint generateConstraint() {
//        String source = getControlAction().getController().getName();
//        String typeAndCA = getTypeAndControlActionString();
//        String context = getContextString();
//
//        String scName = source + " must not " + typeAndCA + " when " + context;
//
//        return new SafetyConstraint(scName, this);
//    }
//    public String getTypeAndControlActionString() {
//        return switch (getType()) {
//            case PROVIDED -> "provide " + getControlAction().getName();
//            case NOT_PROVIDED -> "not provide " + getControlAction().getName();
//            case TOO_EARLY -> "provide " + getControlAction().getName() + " too early";
//            case TOO_LATE -> "provide " + getControlAction().getName() + " too late";
//            case OUT_OF_ORDER -> "provide " + getControlAction().getName() + " out of order";
//            case STOPPED_TOO_SOON -> "stop providing " + getControlAction().getName() + " too soon";
//            case APPLIED_TOO_LONG -> "provide " + getControlAction().getName() + " too long";
//        };
//    }
//    public String getContextString() {
//        StringJoiner context = new StringJoiner(" AND ");
//        for (Value value : values) {
//            context.add(value.toString());
//        }
//        return context.toString();
//    }

    // ------------------------------------------------
}
