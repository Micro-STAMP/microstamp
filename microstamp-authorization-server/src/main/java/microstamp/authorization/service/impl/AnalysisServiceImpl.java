package microstamp.authorization.service.impl;

import microstamp.authorization.dto.AnalysisInsertDto;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.AnalysisUpdateDto;
import microstamp.authorization.entity.Analysis;
import microstamp.authorization.entity.User;
import microstamp.authorization.exception.NotFoundException;
import microstamp.authorization.mapper.AnalysisMapper;
import microstamp.authorization.repository.AnalysisRepository;
import microstamp.authorization.repository.UserRepository;
import microstamp.authorization.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private UserRepository userRepository;

    public List<AnalysisReadDto> findAll() {
        return analysisRepository.findAll().stream()
                .map(AnalysisMapper::toDto)
                .sorted(Comparator.comparing(AnalysisReadDto::getCreationDate))
                .toList();
    }

    public AnalysisReadDto findById(UUID id) throws NotFoundException {
        return AnalysisMapper.toDto(analysisRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Analysis", id.toString())));
    }

    public List<AnalysisReadDto> findByUserId(UUID id) {
        return analysisRepository.findByUserId(id).stream()
                .map(AnalysisMapper::toDto)
                .sorted(Comparator.comparing(AnalysisReadDto::getCreationDate))
                .toList();
    }

    public AnalysisReadDto insert(AnalysisInsertDto analysisInsertDto) throws NotFoundException {
        User user = userRepository.findById(analysisInsertDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User", analysisInsertDto.getUserId().toString()));

        Analysis analysis = AnalysisMapper.toEntity(analysisInsertDto);
        analysis.setUser(user);

        analysisRepository.save(analysis);

        return AnalysisMapper.toDto(analysis);
    }

    public void update(UUID id, AnalysisUpdateDto analysisUpdateDto) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Analysis", id.toString()));

        analysis.setName(analysisUpdateDto.getName());
        analysis.setDescription(analysisUpdateDto.getDescription());

        analysisRepository.save(analysis);
    }

    public void delete(UUID id) throws NotFoundException {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Analysis", id.toString()));

        analysisRepository.deleteById(analysis.getId());
    }
}
