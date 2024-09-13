package step3.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.dto.step2.VariableReadDto;
import step3.entity.association.UnsafeControlActionState;
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
    private UUID controlActionId;

    @OneToMany(
            mappedBy = "unsafeControlAction",
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

    public String generateName(Step2Proxy step2Proxy) {
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(this.controlActionId);


        String source = controlAction.connection().source().name();
        String typeAndCA = getTypeAndControlActionString(controlAction.name());
        String context = generateContextString(step2Proxy);

        return source + " " + typeAndCA + " when " + context;
    }

    public String generateConstraintName(Step2Proxy step2Proxy) {
        var controlAction = step2Proxy.getControlActionById(this.getControlActionId());

        String source = controlAction.connection().source().name();
        String typeAndCA = getTypeAndControlActionString(controlAction.name());
        String context = generateContextString(step2Proxy);

        return source + " must not " + typeAndCA + " when " + context;
    }

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
}
