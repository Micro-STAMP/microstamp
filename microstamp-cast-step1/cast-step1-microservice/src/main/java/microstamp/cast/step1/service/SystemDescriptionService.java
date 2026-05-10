package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.systemdescription.SystemDescriptionInsertDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionReadDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionUpdateDto;

import java.util.List;
import java.util.UUID;

public interface SystemDescriptionService {

    List<SystemDescriptionReadDto> findAll();

    SystemDescriptionReadDto findById(UUID id);

    List<SystemDescriptionReadDto> findByAnalysisId(UUID id);

    SystemDescriptionReadDto insert(SystemDescriptionInsertDto systemDescriptionInsertDto);

    void update(UUID id, SystemDescriptionUpdateDto systemDescriptionUpdateDto);

    void delete(UUID id);
}