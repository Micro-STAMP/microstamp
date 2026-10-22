package microstamp.cast.step3.service.impl;

import lombok.RequiredArgsConstructor;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionInsertDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionReadDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionUpdateDto;
import microstamp.cast.step3.entity.ControlActionContribution;
import microstamp.cast.step3.exception.ControlActionContributionNotFoundException;
import microstamp.cast.step3.mapper.ControlActionContributionMapper;
import microstamp.cast.step3.repository.ControlActionContributionRepository;
import microstamp.cast.step3.service.AuditLogService;
import microstamp.cast.step3.service.ControlActionContributionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ControlActionContributionServiceImpl implements ControlActionContributionService {

    private final ControlActionContributionRepository repository;
    private final ControlActionContributionMapper mapper;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public ControlActionContributionReadDto create(ControlActionContributionInsertDto dto) {
        ControlActionContribution entity = mapper.toEntity(dto);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    public List<ControlActionContributionReadDto> findByAnalysisId(UUID analysisId) {
        return repository.findByAnalysisId(analysisId).stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public ControlActionContributionReadDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toReadDto)
                .orElseThrow(() -> new ControlActionContributionNotFoundException("CAC not found"));
    }

    @Override
    @Transactional
    public ControlActionContributionReadDto update(UUID id, ControlActionContributionUpdateDto dto) {
        ControlActionContribution entity = repository.findById(id)
                .orElseThrow(() -> new ControlActionContributionNotFoundException("CAC not found"));
        mapper.updateEntityFromDto(dto, entity);
        ControlActionContribution saved = repository.save(entity);
        auditLogService.record(saved.getAnalysisId(), "Step 3", "Control Action Contribution " + saved.getCode());
        return mapper.toReadDto(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
