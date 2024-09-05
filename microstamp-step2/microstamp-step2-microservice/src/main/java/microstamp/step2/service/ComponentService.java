package microstamp.step2.service;

import microstamp.step2.dto.component.*;
import microstamp.step2.enumeration.ComponentType;

import java.util.List;
import java.util.UUID;

public interface ComponentService {

    List<ComponentReadDto> findAll();

    List<ComponentReadDto> findAll(ComponentType componentType);

    ComponentReadDto findById(UUID id);

    ComponentReadDto findById(UUID id, ComponentType componentType);

    List<ComponentReadDto> findByAnalysisId(UUID id);

    List<ComponentReadDto> findByAnalysisId(UUID id, ComponentType componentType);

    ComponentReadDto insert(ComponentInsertDto componentInsertDto);

    ComponentReadDto insert(ComponentBaseInsertDto componentBaseInsertDto, ComponentType componentType);

    void update(UUID id, ComponentUpdateDto componentUpdateDto);

    void update(UUID id, ComponentUpdateDto componentUpdateDto, ComponentType componentType);

    void delete(UUID id);

    void delete(UUID id, ComponentType componentType);

    List<ComponentReadDto> getComponentChildren(UUID id);

    ComponentDependenciesDto getComponentDependencies(UUID id);

}
