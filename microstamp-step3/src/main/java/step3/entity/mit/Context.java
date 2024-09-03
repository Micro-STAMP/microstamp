package step3.entity.mit;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.dto.mit.step2.StateReadDto;
import step3.dto.mit.step2.VariableReadDto;
import step3.entity.mit.association.ContextState;
import step3.proxy.Step2Proxy;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Table(name = "context")
@Entity(name = "Context")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Context {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @OneToMany(
            mappedBy = "context",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ContextState> stateAssociations = new ArrayList<>();

    @ManyToOne @JoinColumn(name = "context_table_id")
    private ContextTable contextTable;

    public Context(List<UUID> statesId) {
        for (UUID stateId : statesId) {
            ContextState contextState = ContextState.builder()
                    .stateId(stateId)
                    .context(this)
                    .build();
            this.stateAssociations.add(contextState);
        }
    }

    public String generateContext(Step2Proxy step2) {
        List<VariableReadDto> variables = step2.getAllVariables();
        List<UUID> contextStatesIds = stateAssociations.stream()
                .map(ContextState::getStateId)
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
