package microstamp.step2.service.impl;

import microstamp.step2.client.MicroStampAuthClient;
import microstamp.step2.dto.component.*;
import microstamp.step2.dto.variable.VariableReadDto;
import microstamp.step2.entity.*;
import microstamp.step2.enumeration.ComponentType;
import microstamp.step2.exception.Step2EnvironmentParentException;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.exception.Step2OrphanException;
import microstamp.step2.exception.Step2SelfParentingComponentException;
import microstamp.step2.mapper.ComponentMapper;
import microstamp.step2.mapper.ConnectionMapper;
import microstamp.step2.mapper.VariableMapper;
import microstamp.step2.repository.*;
import microstamp.step2.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private MicroStampAuthClient microStampAuthClient;

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private ControlledProcessRepository controlledProcessRepository;

    @Autowired
    private ActuatorRepository actuatorRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private VariableRepository variableRepository;

    public List<ComponentReadDto> findAll() {
        return componentRepository.findAll().stream()
                .map(ComponentMapper::toDto)
                .sorted(Comparator.comparing(ComponentReadDto::getCode))
                .toList();
    }

    public List<ComponentReadDto> findAll(ComponentType componentType) {
        List<? extends microstamp.step2.entity.Component> components = switch (componentType){
            case CONTROLLER -> controllerRepository.findAll();
            case CONTROLLED_PROCESS -> controlledProcessRepository.findAll();
            case ACTUATOR -> actuatorRepository.findAll();
            case SENSOR -> sensorRepository.findAll();
        };

        return components.stream()
                .map(ComponentMapper::toDto)
                .sorted(Comparator.comparing(ComponentReadDto::getCode))
                .toList();
    }

    public ComponentReadDto findById(UUID id) throws Step2NotFoundException {
        return ComponentMapper.toDto(findComponentById(id));
    }

    public ComponentReadDto findById(UUID id, ComponentType componentType) throws Step2NotFoundException {
        return ComponentMapper.toDto(findComponentById(id, componentType));
    }

    public List<ComponentReadDto> findByAnalysisId(UUID id) {
        return componentRepository.findByAnalysisId(id).stream()
                .map(ComponentMapper::toDto)
                .sorted(Comparator.comparing(ComponentReadDto::getCode))
                .toList();
    }

    public List<ComponentReadDto> findByAnalysisId(UUID id, ComponentType componentType) {
        List<? extends microstamp.step2.entity.Component> components = switch (componentType){
            case CONTROLLER -> controllerRepository.findByAnalysisId(id);
            case CONTROLLED_PROCESS -> controlledProcessRepository.findByAnalysisId(id);
            case ACTUATOR -> actuatorRepository.findByAnalysisId(id);
            case SENSOR -> sensorRepository.findByAnalysisId(id);
        };

        return components.stream()
                .map(ComponentMapper::toDto)
                .sorted(Comparator.comparing(ComponentReadDto::getCode))
                .toList();
    }

    public ComponentReadDto insert(ComponentInsertDto componentInsertDto) throws RuntimeException {
        microStampAuthClient.getAnalysisById(componentInsertDto.getAnalysisId());

        microstamp.step2.entity.Component father = componentInsertDto.getFatherId() != null
                ? findComponentById(componentInsertDto.getFatherId())
                : null;

        if(father instanceof Environment)
            throw new Step2EnvironmentParentException();

        microstamp.step2.entity.Component component = ComponentMapper.toEntity(componentInsertDto, father);
        componentRepository.save(component);

        return ComponentMapper.toDto(component);
    }

    public ComponentReadDto insert(ComponentBaseInsertDto componentBaseInsertDto, ComponentType componentType) throws Step2NotFoundException {

        ComponentInsertDto componentInsertDto = new ComponentInsertDto(componentBaseInsertDto, componentType);
        componentInsertDto.setType(componentType);

        return insert(componentInsertDto);
    }

    public void update(UUID id, ComponentUpdateDto componentUpdateDto) throws Step2NotFoundException {
        microstamp.step2.entity.Component component = findComponentById(id);
        updateComponent(component, componentUpdateDto);
    }

    public void update(UUID id, ComponentUpdateDto componentUpdateDto, ComponentType componentType) throws Step2NotFoundException {
        microstamp.step2.entity.Component component = findComponentById(id,componentType);
        updateComponent(component, componentUpdateDto);
    }

    public void delete(UUID id) {
        deleteComponent(id);
    }

    public void delete(UUID id, ComponentType componentType) throws Step2NotFoundException{
        findById(id, componentType);
        deleteComponent(id);
    }

    public List<ComponentReadDto> getComponentChildren(UUID id) throws Step2NotFoundException {
        microstamp.step2.entity.Component component = componentRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Component", id.toString()));

        List<microstamp.step2.entity.Component> children = new ArrayList<>();
        getChildren(component, children);

        return children.stream()
                .map(ComponentMapper::toDto)
                .sorted(Comparator.comparing(ComponentReadDto::getCode))
                .toList();
    }

    public ComponentDependenciesDto getComponentDependencies(UUID id) throws Step2NotFoundException {
        microstamp.step2.entity.Component component = componentRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Component", id.toString()));

        ComponentDependenciesDto dependenciesDto = new ComponentDependenciesDto();
        Set<Connection> seen = new HashSet<>();

        getDependencies(component, dependenciesDto, seen);

        return dependenciesDto;
    }

    private microstamp.step2.entity.Component findComponentById(UUID id) throws Step2NotFoundException {
        return componentRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Component", id.toString()));
    }

    private microstamp.step2.entity.Component findComponentById(UUID id, ComponentType componentType) throws Step2NotFoundException {
        return switch (componentType){
            case CONTROLLER -> controllerRepository.findById(id)
                    .orElseThrow(() -> new Step2NotFoundException("Controller", id.toString()));
            case CONTROLLED_PROCESS -> controlledProcessRepository.findById(id)
                    .orElseThrow(() -> new Step2NotFoundException("ControlledProcess", id.toString()));
            case ACTUATOR -> actuatorRepository.findById(id)
                    .orElseThrow(() -> new Step2NotFoundException("Actuator", id.toString()));
            case SENSOR -> sensorRepository.findById(id)
                    .orElseThrow(() -> new Step2NotFoundException("Sensor", id.toString()));
        };
    }

    private void updateComponent(microstamp.step2.entity.Component component, ComponentUpdateDto componentUpdateDto) throws Step2NotFoundException {
        if (componentUpdateDto.getFatherId() != null) {

            if (componentUpdateDto.getFatherId().equals(component.getId()))
                throw new Step2SelfParentingComponentException();

            microstamp.step2.entity.Component father = findComponentById(componentUpdateDto.getFatherId());

            if(father instanceof Environment)
                throw new Step2EnvironmentParentException();

            List<ComponentReadDto> children = getComponentChildren(component.getId());

            if(children.stream().anyMatch(c -> c.getId() == father.getId()))
                throw new Step2OrphanException();

            component.setFather(father);
        } else {
            component.setFather(null);
        }

        component.setName(componentUpdateDto.getName());
        component.setCode(componentUpdateDto.getCode());
        component.setBorder(componentUpdateDto.getBorder());
        component.setIsVisible(componentUpdateDto.getIsVisible());

        componentRepository.save(component);

        String componentTypeName = componentUpdateDto.getType().getFormattedName();
        componentRepository.updateType(component.getId().toString(), componentTypeName);
    }

    private void deleteComponent(UUID id){
        List<microstamp.step2.entity.Component> children = componentRepository.findChildrenByComponentId(id.toString());
        children.forEach(c -> delete(c.getId()));

        List<Connection> connectionsSource = connectionRepository.findBySourceId(id);
        connectionsSource.forEach(connection -> connectionRepository.deleteById(connection.getId()));

        List<Connection> connectionsTarget = connectionRepository.findByTargetId(id);
        connectionsTarget.forEach(connection -> connectionRepository.deleteById(connection.getId()));

        componentRepository.deleteById(id);
    }

    private void getChildren(microstamp.step2.entity.Component parent, List<microstamp.step2.entity.Component> children) {
        List<microstamp.step2.entity.Component> directChildren = componentRepository.findChildrenByComponentId(parent.getId().toString());
        for (microstamp.step2.entity.Component child : directChildren) {
            children.add(child);
            getChildren(child, children);
        }
    }

    private void getDependencies(microstamp.step2.entity.Component component, ComponentDependenciesDto dependenciesDto, Set<Connection> seen) {
        dependenciesDto.getComponents().add(ComponentMapper.toDto(component));
        List<Connection> connectionsSource = connectionRepository.findBySourceId(component.getId());
        for (Connection connection : connectionsSource) {
            if (!seen.contains(connection)) {
                dependenciesDto.getConnections().add(ConnectionMapper.toDto(connection));
                seen.add(connection);
            }
        }

        List<Connection> connectionsTarget = connectionRepository.findByTargetId(component.getId());
        for (Connection connection : connectionsTarget) {
            if (!seen.contains(connection)) {
                dependenciesDto.getConnections().add(ConnectionMapper.toDto(connection));
                seen.add(connection);
            }
        }

        dependenciesDto.getVariables().addAll(variableRepository.findByComponentId(component.getId()).stream()
                .map(VariableMapper::toDto)
                .sorted(Comparator.comparing(VariableReadDto::getCode))
                .toList());

        List<microstamp.step2.entity.Component> children = componentRepository.findChildrenByComponentId(component.getId().toString());
        for (microstamp.step2.entity.Component child : children)
            getDependencies(child, dependenciesDto, seen);
    }
}