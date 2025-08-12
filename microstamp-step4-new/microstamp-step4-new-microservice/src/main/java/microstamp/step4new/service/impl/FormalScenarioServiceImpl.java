package microstamp.step4new.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.client.MicroStampStep3Client;
import microstamp.step4new.client.MicroStampAuthClient;
import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;
import microstamp.step4new.entity.FormalScenario;
import microstamp.step4new.entity.FormalScenarioClass;
import microstamp.step4new.constants.FormalScenarioCodes;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.helper.FormalScenarioHelper;
import microstamp.step4new.repository.FormalScenarioRepository;
import microstamp.step4new.service.FormalScenarioService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class FormalScenarioServiceImpl implements FormalScenarioService {

    private final FormalScenarioRepository repository;
    private final MicroStampStep3Client step3Client;
    private final MicroStampAuthClient authClient;

    @Override
    public FormalScenarioReadDto create(UUID analysisId, UUID unsafeControlActionId) {
        log.info("Request received to create FormalScenario for analysis {} and UCA {}", analysisId, unsafeControlActionId);

        repository.findByUnsafeControlActionId(unsafeControlActionId)
                .ifPresent(existing -> { throw new microstamp.step4new.exception.Step4NewIllegalArgumentException("FormalScenario already exists for UCA: " + unsafeControlActionId); });

        authClient.getAnalysisById(analysisId);
        UnsafeControlActionFullReadDto uca = step3Client.readUnsafeControlAction(unsafeControlActionId);

        FormalScenario entity = new FormalScenario();
        entity.setUnsafeControlActionId(unsafeControlActionId);
        entity.setAnalysisId(analysisId);

        entity.addClass(FormalScenarioClass.builder()
                .code(FormalScenarioCodes.CLASS1)
                .build());
        entity.addClass(FormalScenarioClass.builder()
                .code(FormalScenarioCodes.CLASS2)
                .build());
        entity.addClass(FormalScenarioClass.builder()
                .code(FormalScenarioCodes.CLASS3)
                .build());
        entity.addClass(FormalScenarioClass.builder()
                .code(FormalScenarioCodes.CLASS4)
                .build());

        repository.save(entity);

        log.info("FormalScenario created for UCA {} with id {}", unsafeControlActionId, entity.getId());
        return FormalScenarioHelper.buildScenario(uca);
    }

    @Override
    public FormalScenarioReadDto getOrCreate(UUID unsafeControlActionId) {
        UnsafeControlActionFullReadDto uca = step3Client.readUnsafeControlAction(unsafeControlActionId);
        return repository.findByUnsafeControlActionId(unsafeControlActionId)
                .map(existing -> FormalScenarioHelper.buildScenario(uca))
                .orElseGet(() -> create(uca.analysis_id(), unsafeControlActionId));
    }

    @Override
    public FormalScenarioReadDto findById(UUID id) {
        log.info("Finding FormalScenario by id: {}", id);
        return repository.findById(id)
                .map(existing -> {
                    UnsafeControlActionFullReadDto uca = step3Client.readUnsafeControlAction(existing.getUnsafeControlActionId());
                    return FormalScenarioHelper.buildScenario(uca);
                })
                .orElseThrow(() -> new Step4NewNotFoundException("FormalScenario", id.toString()));
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting FormalScenario by id: {}", id);
        repository.deleteById(id);
    }
}
