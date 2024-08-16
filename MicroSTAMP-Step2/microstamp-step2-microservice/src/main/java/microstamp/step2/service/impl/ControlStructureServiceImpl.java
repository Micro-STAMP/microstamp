package microstamp.step2.service.impl;

import microstamp.step2.dto.component.ComponentReadDto;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.controlstructure.ControlStructureReadDto;
import microstamp.step2.dto.image.ImageReadDto;
import microstamp.step2.service.ComponentService;
import microstamp.step2.service.ConnectionService;
import microstamp.step2.service.ControlStructureService;
import microstamp.step2.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ControlStructureServiceImpl implements ControlStructureService {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private ImageService imageService;

    public ControlStructureReadDto findByAnalysisId(UUID id) {
        List<ComponentReadDto> components = componentService.findByAnalysisId(id);
        List<ConnectionReadDto> connections = connectionService.findByAnalysisId(id);
        List<ImageReadDto> images = imageService.findByAnalysisId(id);

        return new ControlStructureReadDto(components, connections, images);
    }
}
