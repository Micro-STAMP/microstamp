package microstamp.step2.service;

import microstamp.step2.dto.image.ImageReadDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ImageService {

    List<ImageReadDto> findAll();

    ImageReadDto findById(UUID id);

    List<ImageReadDto> findByAnalysisId(UUID id);

    ImageReadDto insert(UUID analysisId, MultipartFile file) throws Exception;

    void delete(UUID id);

}
