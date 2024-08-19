package step3.entity.mit;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.dto.mit.step2.ControlActionReadDto;
import step3.dto.mit.step2.StateReadDto;
import step3.dto.mit.step2.VariableReadDto;
import step3.entity.mit.association.UnsafeControlActionState;
import step3.proxy.Step2Proxy;

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
    private UUID controllerId; //v ver depois onde é o melhor lugar para isso

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
//    @JoinTable(
//            name = "unsafe_control_action_state",
//            joinColumns = @JoinColumn(name = "unsafe_control_action_id"),
//            inverseJoinColumns = @JoinColumn(name = "state_id")
//    )
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
    public String generateName(Step2Proxy step2Proxy) {
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(this.controlActionId);

        String source = controlAction.name();
        String typeAndCA = getTypeAndControlActionString(controlAction.name());
        String context = generateContextString(step2Proxy);

        return source + " " + typeAndCA + " when " + context;
    }

//    public SafetyConstraint generateConstraint() {
//        String source = getControlAction().getController().getName();
//        String typeAndCA = getTypeAndControlActionString();
//        String context = getContextString();
//
//        String scName = source + " must not " + typeAndCA + " when " + context;
//
//        return new SafetyConstraint(scName, this);
//    }

    public String getTypeAndControlActionString(String controlActionName) {
        return switch (getType()) {
            case PROVIDED -> "provide " + controlActionName;
            case NOT_PROVIDED -> "not provide " + controlActionName;
            case TOO_EARLY -> "provide " + controlActionName + " too early";
            case TOO_LATE -> "provide " + controlActionName + " too late";
            case OUT_OF_ORDER -> "provide " + controlActionName + " out of order";
            case STOPPED_TOO_SOON -> "stop providing " + controlActionName + " too soon";
            case APPLIED_TOO_LONG -> "provide " + controlActionName + " too long";
        };
    }

//    public String getContextString() {
//        StringJoiner context = new StringJoiner(" AND ");
//        for (Value value : values) {
//            context.add(value.toString());
//        }
//        return context.toString();
//    }

    public String generateContextString(Step2Proxy step2) {
        List<VariableReadDto> variables = step2.getAllVariables();
        List<UUID> contextStatesIds = stateAssociations.stream()
                .map(UnsafeControlActionState::getStateId)
                .toList();
        StringJoiner context = new StringJoiner(" AND ");

        for (VariableReadDto variable : variables) {
            List<StateReadDto> states = variable.states();

            for (StateReadDto state : states){
                if (contextStatesIds.contains(state.id())){
                    String contextModel = "{ " + variable.name() + " = " + state.name() + " }";
                    context.add(contextModel);
                }
            }
        }

        return context.toString();
    }

    // ------------------------------------------------
}
