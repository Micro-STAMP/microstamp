package microstamp.authorization.unit;

import lombok.SneakyThrows;
import microstamp.authorization.dto.ImageReadDto;
import microstamp.authorization.entity.Analysis;
import microstamp.authorization.entity.Image;
import microstamp.authorization.exception.AnalysisAlreadyHasImageException;
import microstamp.authorization.exception.NotFoundException;
import microstamp.authorization.repository.AnalysisRepository;
import microstamp.authorization.repository.ImageRepository;
import microstamp.authorization.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplUnitTest {

    @InjectMocks
    private ImageServiceImpl service;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private AnalysisRepository analysisRepository;

    @Test
    @DisplayName("#findByAnalysisId > When no image is found > Throw an exception")
    void findByAnalysisIdWhenNoImageIsFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(imageRepository.findByAnalysisId(mockAnalysisId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findByAnalysisId(mockAnalysisId));
    }

    @Test
    @DisplayName("#findByAnalysisId > When the image is found > Return it")
    void findByAnalysisIdWhenTheImageIsFoundReturnIt() {
        Image mock = assembleImage.get();

        when(imageRepository.findByAnalysisId(mock.getAnalysis().getId())).thenReturn(Optional.of(mock));

        ImageReadDto response = service.findByAnalysisId(mock.getAnalysis().getId());

        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getFileName(), response.getFileName())
        );
    }

    @Test
    @DisplayName("#insert > When the file is empty > Throw an exception")
    void insertWhenTheFileIsEmptyThrowAnException() {
        assertThrows(Exception.class, () -> service.insert(UUID.randomUUID(), new MockMultipartFile("File", new byte[] {})));
    }

    @Test
    @DisplayName("#insert > When the original file name is empty > Throw an exception")
    void insertWhenTheOriginalFileNameIsEmptyThrowAnException() {
        assertThrows(Exception.class, () -> service.insert(UUID.randomUUID(), new MockMultipartFile("File", HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d"))));
    }

    @Test
    @DisplayName("#insert > When no analysis is found > Throw an exception")
    void insertWhenNoAnalysisIsFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();
        
        when(analysisRepository.findById(mockAnalysisId)).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(Exception.class, () ->
                        service.insert(mockAnalysisId,
                                new MockMultipartFile("File",
                                        "Original file name",
                                        "png",
                                        HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d")))
                ),
                () -> verify(analysisRepository, times(1)).findById(mockAnalysisId)
        );
    }

    @Test
    @DisplayName("#insert > When the analysis already has an image > Throw an exception")
    void insertWhenTheAnalysisAlreadyHasAnImageThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(analysisRepository.findById(mockAnalysisId)).thenReturn(Optional.of(assembleAnalysis.get()));

        assertAll(
                () -> assertThrows(AnalysisAlreadyHasImageException.class, () ->
                        service.insert(mockAnalysisId,
                                new MockMultipartFile("File",
                                        "Original file name",
                                        "png",
                                        HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d")))
                ),
                () -> verify(analysisRepository, times(1)).findById(mockAnalysisId)
        );
    }

    @Test
    @SneakyThrows
    @DisplayName("#insert > When the analysis does not have an image > Insert the image")
    void insertWhenTheAnalysisDoesNotHaveAnImageInsertTheImage() {
        UUID mockAnalysisId = UUID.randomUUID();
        Analysis mockAnalysis = assembleAnalysis.get();
        mockAnalysis.setImage(null);

        when(analysisRepository.findById(mockAnalysisId)).thenReturn(Optional.of(mockAnalysis));

        ImageReadDto response = service.insert(mockAnalysisId,
                new MockMultipartFile("File",
                        "Original file name",
                        "png",
                        HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d")));

        assertAll(
                () -> assertEquals("Original file name", response.getFileName()),
                () -> verify(analysisRepository, times(1)).findById(mockAnalysisId)
        );
    }

    @Test
    @DisplayName("#deleteByAnalysisId > When no analysis is found > Throw an exception")
    void deleteByAnalysisIdWhenNoAnalysisIsFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(analysisRepository.findById(mockAnalysisId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteByAnalysisId(mockAnalysisId));
    }

    @Test
    @DisplayName("#deleteByAnalysisId > When the analysis has no image > Throw an exception")
    void deleteByAnalysisIdWhenTheAnalysisHasNoImageThrowAnException() {
        Analysis mock = assembleAnalysis.get();
        mock.setImage(null);

        when(analysisRepository.findById(mock.getId())).thenReturn(Optional.of(mock));

        assertThrows(NotFoundException.class, () -> service.deleteByAnalysisId(mock.getId()));
    }

    @Test
    @DisplayName("deleteByAnalysisId > When the analysis has an image > Delete the image")
    void deleteByAnalysisIdWhenTheAnalysisHasAnImageDeleteTheImage() {
        Analysis mock = assembleAnalysis.get();
        UUID imageId = mock.getImage().getId();

        when(analysisRepository.findById(mock.getId())).thenReturn(Optional.of(mock));
        when(analysisRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        service.deleteByAnalysisId(mock.getId());

        verify(analysisRepository, times(1)).findById(mock.getId());
        verify(imageRepository, times(1)).deleteById(imageId);
    }


    private final Supplier<Image> assembleImage = () -> Image.builder()
            .id(UUID.randomUUID())
            .fileName("Mock file name")
            .analysis(Analysis.builder().id(UUID.randomUUID()).build())
            .data(new byte[] {})
            .build();

    private final Supplier<Analysis> assembleAnalysis = () -> Analysis.builder()
            .id(UUID.randomUUID())
            .name("Mock analysis")
            .image(Image.builder()
                    .id(UUID.randomUUID())
                    .fileName("image.png")
                    .build())
            .build();

}
