package microstamp.step2.service.impl;

import microstamp.step2.client.MicroStampStep1Client;
import microstamp.step2.dto.responsibility.ResponsibilityReadDto;
import microstamp.step2.dto.responsibility.ResponsibilityUpdateDto;
import microstamp.step2.entity.Environment;
import microstamp.step2.entity.Responsibility;
import microstamp.step2.dto.responsibility.ResponsibilityInsertDto;
import microstamp.step2.exception.Step2EnvironmentResponsibilityException;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.ResponsibilityMapper;
import microstamp.step2.repository.ComponentRepository;
import microstamp.step2.repository.ResponsibilityRepository;
import microstamp.step2.service.ResponsibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class ResponsibilityServiceImpl implements ResponsibilityService {

    @Autowired
    private MicroStampStep1Client microStampStep1Client;

    @Autowired
    private ResponsibilityRepository responsibilityRepository;

    @Autowired
    private ComponentRepository componentRepository;

    public List<ResponsibilityReadDto> findAll() {
        return responsibilityRepository.findAll().stream()
                .map(ResponsibilityMapper::toDto)
                .sorted(Comparator.comparing(ResponsibilityReadDto::getCode))
                .toList();
    }

    public ResponsibilityReadDto findById(UUID id) throws Step2NotFoundException {
        return ResponsibilityMapper.toDto(responsibilityRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Responsibility", id.toString())));
    }

    public ResponsibilityReadDto insert(ResponsibilityInsertDto responsibilityInsertDto) throws Step2EnvironmentResponsibilityException {
        microstamp.step2.entity.Component component = componentRepository.findById(responsibilityInsertDto.getComponentId())
                .orElseThrow(() -> new Step2NotFoundException("Component", responsibilityInsertDto.getComponentId().toString()));

        if (component instanceof Environment)
            throw new Step2EnvironmentResponsibilityException();

        microStampStep1Client.getSystemSafetyConstraintById(responsibilityInsertDto.getSystemSafetyConstraintId());

        Responsibility responsibility = ResponsibilityMapper.toEntity(responsibilityInsertDto, component);
        responsibilityRepository.save(responsibility);

        return ResponsibilityMapper.toDto(responsibility);
    }

    public void update(UUID id, ResponsibilityUpdateDto responsibilityUpdateDto) throws Step2NotFoundException {
        Responsibility responsibility = responsibilityRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Responsibility", id.toString()));

        microStampStep1Client.getSystemSafetyConstraintById(responsibilityUpdateDto.getSystemSafetyConstraintId());

        responsibility.setResponsibility(responsibilityUpdateDto.getResponsibility());
        responsibility.setCode(responsibilityUpdateDto.getCode());
        responsibility.setSystemSafetyConstraintId(responsibilityUpdateDto.getSystemSafetyConstraintId());

        responsibilityRepository.save(responsibility);
    }

    public void delete(UUID id) throws Step2NotFoundException {
        Responsibility responsibility = responsibilityRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Responsibility", id.toString()));
        responsibilityRepository.deleteById(responsibility.getId());
    }
}
