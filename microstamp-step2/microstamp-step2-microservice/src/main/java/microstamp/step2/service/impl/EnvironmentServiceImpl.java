package microstamp.step2.service.impl;

import microstamp.step2.client.MicroStampAuthClient;
import microstamp.step2.dto.environment.EnvironmentReadDto;
import microstamp.step2.entity.Environment;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.exception.Step2UniqueEnvironmentException;
import microstamp.step2.mapper.EnvironmentMapper;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.repository.EnvironmentRepository;
import microstamp.step2.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EnvironmentServiceImpl implements EnvironmentService {

    @Autowired
    private MicroStampAuthClient microStampAuthClient;

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    public EnvironmentReadDto findById(UUID id) {
        return EnvironmentMapper.toDto(environmentRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Environment", id.toString())));
    }

    public EnvironmentReadDto findByAnalysisId(UUID id) {
        return EnvironmentMapper.toDto(environmentRepository.findByAnalysisId(id));
    }

    public EnvironmentReadDto insert(UUID analysisId) {
        microStampAuthClient.getAnalysisById(analysisId);

        Environment environment = environmentRepository.findByAnalysisId(analysisId);
        if(environment != null)
            throw new Step2UniqueEnvironmentException();

        environment = EnvironmentMapper.toEntity(analysisId);

        environmentRepository.save(environment);

        return EnvironmentMapper.toDto(environment);
    }

    public void delete(UUID id) {
        Environment environment = environmentRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Environment", id.toString()));

        connectionRepository.findBySourceId(id)
                .forEach(connection -> connectionRepository.deleteById(connection.getId()));

        connectionRepository.findByTargetId(id)
                .forEach(connection -> connectionRepository.deleteById(connection.getId()));

        environmentRepository.deleteById(environment.getId());
    }
}
