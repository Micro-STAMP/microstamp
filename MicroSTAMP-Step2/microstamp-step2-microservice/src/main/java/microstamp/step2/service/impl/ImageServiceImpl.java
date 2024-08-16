package microstamp.step2.service.impl;

import microstamp.step2.client.MicroStampAuthClient;
import microstamp.step2.dto.image.ImageReadDto;
import microstamp.step2.entity.Image;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.ImageMapper;
import microstamp.step2.repository.ImageRepository;
import microstamp.step2.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class ImageServiceImpl implements ImageService {

    @Autowired
    private MicroStampAuthClient microStampAuthClient;

    @Autowired
    private ImageRepository imageRepository;

    public List<ImageReadDto> findAll() {
        return imageRepository.findAll().stream()
                .map(ImageMapper::toDto)
                .sorted(Comparator.comparing(ImageReadDto::getName))
                .toList();
    }

    public ImageReadDto findById(UUID id) throws Step2NotFoundException {
        return ImageMapper.toDto(imageRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Image", id.toString())));
    }

    public List<ImageReadDto> findByAnalysisId(UUID id) {
        return imageRepository.findByAnalysisId(id).stream()
                .map(ImageMapper::toDto)
                .sorted(Comparator.comparing(ImageReadDto::getName))
                .toList();
    }

    public ImageReadDto insert(UUID analysisId, MultipartFile file) throws Exception {
        if (file.isEmpty())
            throw new Exception("MultipartFile is empty");

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null)
            throw new Exception("Original filename is null");

        microStampAuthClient.getAnalysisById(analysisId);

        Image image = new Image();
        image.setName(StringUtils.cleanPath(originalFilename));
        image.setData(file.getBytes());
        image.setAnalysisId(analysisId);

        imageRepository.save(image);

        return ImageMapper.toDto(image);
    }

    public void delete(UUID id) throws Step2NotFoundException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Image", id.toString()));
        imageRepository.deleteById(image.getId());
    }
}
