package microstamp.cast.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.systemboundaryimage.SystemBoundaryImageReadDto;
import microstamp.cast.step1.entity.SystemBoundaryImage;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.mapper.SystemBoundaryImageMapper;
import microstamp.cast.step1.repository.SystemBoundaryImageRepository;
import microstamp.cast.step1.service.SystemBoundaryImageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class SystemBoundaryImageServiceImpl implements SystemBoundaryImageService {

    private final SystemBoundaryImageRepository systemBoundaryImageRepository;

    @Override
    public List<SystemBoundaryImageReadDto> findByAnalysisId(UUID id) {
        log.info("Finding CAST system boundary images by analysis id: {}", id);
        return systemBoundaryImageRepository.findByAnalysisId(id).stream()
                .map(SystemBoundaryImageMapper::toDto)
                .toList();
    }

    @Override
    public SystemBoundaryImageReadDto insert(UUID analysisId, MultipartFile file) throws Exception {
        if (file.isEmpty())
            throw new Exception("MultipartFile is empty");

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null)
            throw new Exception("Original filename is null");

        SystemBoundaryImage image = SystemBoundaryImage.builder()
                .name(StringUtils.cleanPath(originalFilename))
                .data(file.getBytes())
                .analysisId(analysisId)
                .build();

        log.info("Inserting the CAST system boundary image {} on the database", image.getName());
        systemBoundaryImageRepository.save(image);

        return SystemBoundaryImageMapper.toDto(image);
    }

    @Override
    public void delete(UUID id) throws Step1NotFoundException {
        SystemBoundaryImage image = systemBoundaryImageRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemBoundaryImage", id.toString()));
        systemBoundaryImageRepository.deleteById(image.getId());
    }
}
