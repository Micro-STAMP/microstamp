package step3.dto.mapper;

import org.springframework.stereotype.Component;
import step3.dto.not_uca_context.NotUcaContextReadDto;
import step3.dto.step2.StateReadDto;
import step3.entity.NotUnsafeControlActionContext;
import step3.entity.UnsafeControlAction;
import step3.entity.association.NotUcaContextState;
import step3.entity.association.UnsafeControlActionState;
import step3.proxy.Step2Proxy;

import java.util.List;

@Component
public class NotUcaContextMapper {
    private final Step2Proxy step2Proxy;

    public NotUcaContextMapper(Step2Proxy step2Proxy) {
        this.step2Proxy = step2Proxy;
    }

    public NotUcaContextReadDto toNotUcaContextReadDto(NotUnsafeControlActionContext notUcaContext) {
        List<StateReadDto> states = getStates(notUcaContext);

        return NotUcaContextReadDto.builder()
                .id(notUcaContext.getId())
                .analysis_id(notUcaContext.getAnalysisId())
                .controlActionId(notUcaContext.getControlActionId())
                .type(notUcaContext.getType().toString())
                .states(states)
                .build();
    }

    public List<NotUcaContextReadDto> toNotUcaContextReadDtoList(List<NotUnsafeControlActionContext> notUcaContextList) {
        return notUcaContextList.stream()
                .map(this::toNotUcaContextReadDto)
                .toList();
    }

    private List<StateReadDto> getStates(NotUnsafeControlActionContext notUcaContext) {
        return notUcaContext.getStateAssociations().stream()
                .map(NotUcaContextState::getStateId)
                .map(step2Proxy::getStateById)
                .toList();
    }
}
