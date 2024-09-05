package microstamp.step2.service;

import microstamp.step2.dto.responsibility.ResponsibilityInsertDto;
import microstamp.step2.dto.responsibility.ResponsibilityReadDto;
import microstamp.step2.dto.responsibility.ResponsibilityUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ResponsibilityService {

    List<ResponsibilityReadDto> findAll();

    ResponsibilityReadDto findById(UUID id);

    ResponsibilityReadDto insert(ResponsibilityInsertDto responsibilityInsertDto);

    void update(UUID id, ResponsibilityUpdateDto responsibilityUpdateDto);

    void delete(UUID id);

}
