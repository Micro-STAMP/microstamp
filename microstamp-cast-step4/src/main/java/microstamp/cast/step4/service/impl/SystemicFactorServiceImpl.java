package microstamp.cast.step4.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import microstamp.cast.step4.client.CastStep3Client;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorInsertDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorReadDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorUpdateDto;
import microstamp.cast.step4.entity.SystemicFactor;
import microstamp.cast.step4.exception.InadequateControlActionReferenceNotFoundException;
import microstamp.cast.step4.exception.SystemicFactorNotFoundException;
import microstamp.cast.step4.mapper.SystemicFactorMapper;
import microstamp.cast.step4.repository.SystemicFactorRepository;
import microstamp.cast.step4.service.SystemicFactorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemicFactorServiceImpl implements SystemicFactorService {

    private final SystemicFactorRepository repository;
    private final SystemicFactorMapper mapper;
    private final CastStep3Client castStep3Client;

    @Override
    @Transactional
    public SystemicFactorReadDto create(SystemicFactorInsertDto dto) {
        validateInadequateControlActionsExist(dto.inadequateControlActionIds());
        SystemicFactor entity = mapper.toEntity(dto);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    public SystemicFactorReadDto findById(UUID id) {
        return mapper.toReadDto(repository.findById(id)
                .orElseThrow(() -> new SystemicFactorNotFoundException("Systemic factor not found")));
    }

    @Override
    public List<SystemicFactorReadDto> findByAnalysisId(UUID analysisId) {
        return repository.findByAnalysisId(analysisId).stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SystemicFactorReadDto update(UUID id, SystemicFactorUpdateDto dto) {
        SystemicFactor entity = repository.findById(id)
                .orElseThrow(() -> new SystemicFactorNotFoundException("Systemic factor not found"));
        validateInadequateControlActionsExist(dto.inadequateControlActionIds());
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private void validateInadequateControlActionsExist(List<UUID> inadequateControlActionIds) {
        if (inadequateControlActionIds == null) {
            return;
        }
        for (UUID icaId : inadequateControlActionIds) {
            try {
                castStep3Client.readInadequateControlAction(icaId);
            } catch (FeignException.NotFound ex) {
                throw new InadequateControlActionReferenceNotFoundException("Inadequate Control Action not found: " + icaId);
            }
        }
    }
}
