package microstamp.authorization.service;

import microstamp.authorization.dto.ImageReadDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageService {

    ImageReadDto findByAnalysisId(UUID id);

    ImageReadDto insert(UUID analysisId, MultipartFile file) throws Exception;

    void deleteByAnalysisId(UUID id);
}
