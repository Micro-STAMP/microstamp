package microstamp.authorization.service.impl;

import lombok.AllArgsConstructor;
import microstamp.authorization.dto.ImageReadDto;
import microstamp.authorization.entity.Analysis;
import microstamp.authorization.entity.Image;
import microstamp.authorization.exception.AnalysisAlreadyHasImageException;
import microstamp.authorization.exception.NotFoundException;
import microstamp.authorization.mapper.ImageMapper;
import microstamp.authorization.repository.AnalysisRepository;
import microstamp.authorization.repository.ImageRepository;
import microstamp.authorization.service.ImageService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final AnalysisRepository analysisRepository;

    public ImageReadDto findByAnalysisId(UUID id) {
        return ImageMapper.toDto(imageRepository.findByAnalysisId(id)
                .orElseThrow(() -> new NotFoundException("Image", id.toString())));
    }

    public ImageReadDto insert(UUID analysisId, MultipartFile file) throws Exception {
        if (file.isEmpty())
            throw new Exception("MultipartFile is empty");

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null)
            throw new Exception("Original filename is null");

        Analysis analysis = analysisRepository.findById(analysisId)
                .orElseThrow(() -> new NotFoundException("Analysis", analysisId.toString()));

        if(analysis.getImage() != null)
            throw new AnalysisAlreadyHasImageException();

        Image image = new Image();
        image.setFileName(StringUtils.cleanPath(originalFilename));
        image.setData(file.getBytes());
        analysis.setImage(image);

        analysisRepository.save(analysis);

        return ImageMapper.toDto(image);
    }

    public void deleteByAnalysisId(UUID id) throws NotFoundException {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Analysis", id.toString()));

        Image image = analysis.getImage();

        if (image == null)
            throw new NotFoundException("Image", id.toString());

        analysis.setImage(null);
        analysisRepository.save(analysis);

        imageRepository.deleteById(image.getId());
    }
}
