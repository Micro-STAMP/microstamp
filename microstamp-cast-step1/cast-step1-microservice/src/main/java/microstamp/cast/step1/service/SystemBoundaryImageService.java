package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.systemboundaryimage.SystemBoundaryImageReadDto;
import microstamp.cast.step1.exception.Step1NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface SystemBoundaryImageService {

    List<SystemBoundaryImageReadDto> findByAnalysisId(UUID id);

    SystemBoundaryImageReadDto insert(UUID analysisId, MultipartFile file) throws Exception;

    void delete(UUID id) throws Step1NotFoundException;
}
