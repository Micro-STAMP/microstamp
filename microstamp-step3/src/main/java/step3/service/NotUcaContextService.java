package step3.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.mapper.NotUcaContextMapper;
import step3.dto.not_uca_context.NotUcaContextCreateDto;
import step3.dto.not_uca_context.NotUcaContextReadDto;
import step3.entity.NotUnsafeControlActionContext;
import step3.entity.UCAType;
import step3.entity.association.NotUcaContextState;
import step3.proxy.AuthServerProxy;
import step3.proxy.Step2Proxy;
import step3.repository.NotUcaContextRepository;
import step3.repository.StateAssociationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NotUcaContextService {
    private final NotUcaContextRepository notUcaContextRepository;
    private final NotUcaContextMapper mapper;
    private final StateAssociationRepository stateAssociationRepository;
    private final AuthServerProxy authServerProxy;
    private final Step2Proxy step2Proxy;

    public NotUcaContextReadDto createNotUcaContext(NotUcaContextCreateDto notUcaContextCreateDto) {
        authServerProxy.getAnalysisById(notUcaContextCreateDto.analysisId());

        NotUnsafeControlActionContext notUcaContext = NotUnsafeControlActionContext.builder()
                .analysisId(notUcaContextCreateDto.analysisId())
                .type(notUcaContextCreateDto.type())
                .build();

        NotUnsafeControlActionContext createdNotUcaContext = notUcaContextRepository.save(notUcaContext);

        List<NotUcaContextState> stateAssociations = new ArrayList<>();
        for (UUID stateId : notUcaContextCreateDto.statesIds()) {
            step2Proxy.getStateById(stateId);

            NotUcaContextState stateAssociation = NotUcaContextState.builder()
                    .notUcaContext(createdNotUcaContext)
                    .stateId(stateId)
                    .build();
            stateAssociations.add(stateAssociation);
        }

        createdNotUcaContext.setStateAssociations(stateAssociations);
        createdNotUcaContext = notUcaContextRepository.save(createdNotUcaContext);

        return mapper.toNotUcaContextReadDto(createdNotUcaContext);
    }

    public List<NotUcaContextReadDto> getNotUcaContextsByAnalysisId(UUID analysisId) {
        authServerProxy.getAnalysisById(analysisId);

        List<NotUnsafeControlActionContext> notUcaContexts = notUcaContextRepository.findAllByAnalysisId(analysisId);

        return mapper.toNotUcaContextReadDtoList(notUcaContexts);
    }

    public void deleteNotUcaContext(UUID notUcaContextId) {
        NotUnsafeControlActionContext notUcaContext = notUcaContextRepository
                .findById(notUcaContextId)
                .orElseThrow(() -> new EntityNotFoundException("Not unsafe control action context not found with id " + notUcaContextId));

        notUcaContextRepository.deleteById(notUcaContextId);
    }

}
