package microstamp.cast.step3.service.impl;

import lombok.RequiredArgsConstructor;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionInsertDto;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionReadDto;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionUpdateDto;
import microstamp.cast.step3.entity.InadequateControlAction;
import microstamp.cast.step3.exception.InadequateControlActionNotFoundException;
import microstamp.cast.step3.mapper.InadequateControlActionMapper;
import microstamp.cast.step3.repository.InadequateControlActionRepository;
import microstamp.cast.step3.service.InadequateControlActionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InadequateControlActionServiceImpl implements InadequateControlActionService {

    private final InadequateControlActionRepository repository;
    private final InadequateControlActionMapper mapper;

    @Override
    @Transactional
    public InadequateControlActionReadDto create(InadequateControlActionInsertDto dto) {
        InadequateControlAction entity = mapper.toEntity(dto);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    public List<InadequateControlActionReadDto> findByAnalysisId(UUID analysisId) {
        return repository.findByAnalysisId(analysisId).stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public InadequateControlActionReadDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toReadDto)
                .orElseThrow(() -> new InadequateControlActionNotFoundException("ICA not found"));
    }

    @Override
    @Transactional
    public InadequateControlActionReadDto update(UUID id, InadequateControlActionUpdateDto dto) {
        InadequateControlAction entity = repository.findById(id)
                .orElseThrow(() -> new InadequateControlActionNotFoundException("ICA not found"));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}