package microstamp.authorization.unit;

import microstamp.authorization.dto.AnalysisInsertDto;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.AnalysisUpdateDto;
import microstamp.authorization.entity.Analysis;
import microstamp.authorization.entity.User;
import microstamp.authorization.exception.NotFoundException;
import microstamp.authorization.repository.AnalysisRepository;
import microstamp.authorization.repository.UserRepository;
import microstamp.authorization.service.impl.AnalysisServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalysisServiceImplUnitTest {
    
    @InjectMocks
    private AnalysisServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnalysisRepository analysisRepository;

    @Test
    @DisplayName("#findAll > When no analysis is found > Return an empty list")
    void findAllWhenNoAnalysisIsFoundReturnAnEmptyList() {
        when(analysisRepository.findAll()).thenReturn(List.of());

        List<AnalysisReadDto> result = service.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are analyses found > Return the list with the analyses")
    void findAllWhenThereAreAnalysesFoundReturnTheListWithTheAnalyses() {
        Analysis mockFirst = assembleAnalysis.get();
        Analysis mockSecond = assembleAnalysis.get();

        when(analysisRepository.findAll()).thenReturn(List.of(mockFirst, mockSecond));

        List<AnalysisReadDto> result = service.findAll();

        assertAll(
                () -> assertEquals(mockFirst.getId(), result.getFirst().getId()),
                () -> assertEquals(mockFirst.getName(), result.getFirst().getName()),
                () -> assertEquals(mockFirst.getCreatedAt(), result.getFirst().getCreationDate()),
                () -> assertEquals(mockFirst.getUser().getId(), result.getFirst().getUserId()),
                () -> assertEquals(mockSecond.getId(), result.getLast().getId()),
                () -> assertEquals(mockSecond.getName(), result.getLast().getName()),
                () -> assertEquals(mockSecond.getCreatedAt(), result.getLast().getCreationDate()),
                () -> assertEquals(mockSecond.getUser().getId(), result.getLast().getUserId())
        );
    }

    @Test
    @DisplayName("#findById > When no analysis is found > Throw an exception")
    void findByIdWhenNoAnalysisIsFoundThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(analysisRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(mockId));
    }

    @Test
    @DisplayName("#findById > When an analysis is found > Return the analysis")
    void findByIdWhenAnAnalysisIsFoundReturnTheAnalysis() {
        Analysis mock = assembleAnalysis.get();

        when(analysisRepository.findById(mock.getId())).thenReturn(Optional.of(mock));

        AnalysisReadDto result = service.findById(mock.getId());

        assertAll(
                () -> assertEquals(mock.getId(), result.getId()),
                () -> assertEquals(mock.getName(), result.getName()),
                () -> assertEquals(mock.getCreatedAt(), result.getCreationDate()),
                () -> assertEquals(mock.getUser().getId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("#findByUserId > When no analysis is found > Return an empty list")
    void findByUserIdWhenNoAnalysisIsFoundReturnAnEmptyList() {
        UUID userId = UUID.randomUUID();

        when(analysisRepository.findByUserId(userId)).thenReturn(List.of());

        List<AnalysisReadDto> result = service.findByUserId(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("#findByUserId > When there are analyses found > Return the list with the analyses")
    void findByUserIdWhenThereAreAnalysesFoundReturnTheListWithTheAnalyses() {
        Analysis mockFirst = assembleAnalysis.get();
        Analysis mockSecond = assembleAnalysis.get();

        when(analysisRepository.findByUserId(mockFirst.getUser().getId())).thenReturn(List.of(mockFirst, mockSecond));

        List<AnalysisReadDto> result = service.findByUserId(mockFirst.getUser().getId());

        assertAll(
                () -> assertEquals(mockFirst.getId(), result.getFirst().getId()),
                () -> assertEquals(mockFirst.getName(), result.getFirst().getName()),
                () -> assertEquals(mockFirst.getCreatedAt(), result.getFirst().getCreationDate()),
                () -> assertEquals(mockFirst.getUser().getId(), result.getFirst().getUserId()),
                () -> assertEquals(mockSecond.getId(), result.getLast().getId()),
                () -> assertEquals(mockSecond.getName(), result.getLast().getName()),
                () -> assertEquals(mockSecond.getCreatedAt(), result.getLast().getCreationDate()),
                () -> assertEquals(mockSecond.getUser().getId(), result.getLast().getUserId())
        );
    }

    @Test
    @DisplayName("#findGuestAnalyses > When no analysis is found > Return an empty list")
    void findGuestAnalysesWhenNoAnalysisIsFoundReturnAnEmptyList() {
        when(analysisRepository.findGuestAnalyses()).thenReturn(List.of());

        List<AnalysisReadDto> result = service.findGuestAnalyses();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("#findGuestAnalyses > When there are analyses found > Return the list with the analyses")
    void findGuestAnalysesWhenThereAreAnalysesFoundReturnTheListWithTheAnalyses() {
        Analysis mockFirst = assembleAnalysis.get();
        Analysis mockSecond = assembleAnalysis.get();

        when(analysisRepository.findGuestAnalyses()).thenReturn(List.of(mockFirst, mockSecond));

        List<AnalysisReadDto> result = service.findGuestAnalyses();

        assertAll(
                () -> assertEquals(mockFirst.getId(), result.getFirst().getId()),
                () -> assertEquals(mockFirst.getName(), result.getFirst().getName()),
                () -> assertEquals(mockFirst.getCreatedAt(), result.getFirst().getCreationDate()),
                () -> assertEquals(mockFirst.getUser().getId(), result.getFirst().getUserId()),
                () -> assertEquals(mockSecond.getId(), result.getLast().getId()),
                () -> assertEquals(mockSecond.getName(), result.getLast().getName()),
                () -> assertEquals(mockSecond.getCreatedAt(), result.getLast().getCreationDate()),
                () -> assertEquals(mockSecond.getUser().getId(), result.getLast().getUserId())
        );
    }

    @Test
    @DisplayName("#insert > When no user is found > Throw an exception")
    void insertWhenNoUserIsFoundThrowAnException() {
        AnalysisInsertDto mock = assembleAnalysisInsert.get();

        when(userRepository.findById(mock.getUserId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.insert(mock));
    }

    @Test
    @DisplayName("#insert > When the user is found > Create the analysis > Return the new analysis")
    void insertWhenTheUserIsFoundCreateTheAnalysisReturnTheNewAnalysis() {
        AnalysisInsertDto mock = assembleAnalysisInsert.get();
        User mockUser = assembleAnalysis.get().getUser();

        when(userRepository.findById(mock.getUserId())).thenReturn(Optional.of(mockUser));
        when(analysisRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AnalysisReadDto result = service.insert(mock);

        assertAll(
                () -> assertEquals(mock.getName(), result.getName()),
                () -> assertEquals(mock.getDescription(), result.getDescription()),
                () -> assertEquals(mockUser.getId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("#update > When the analysis is not found > Throw an exception")
    void updateWhenTheAnalysisIsNotFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(analysisRepository.findById(mockAnalysisId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(mockAnalysisId, assembleAnalysisUpdate.get()));
    }

    @Test
    @DisplayName("#update > When the analysis is found > Update the analysis")
    void updateWhenTheAnalysisIsFoundUpdateTheAnalysis() {
        Analysis mock = assembleAnalysis.get();
        AnalysisUpdateDto mockAnalysisUpdate = assembleAnalysisUpdate.get();

        when(analysisRepository.findById(mock.getId())).thenReturn(Optional.of(mock));
        mock.setName(mockAnalysisUpdate.getName());
        mock.setDescription(mockAnalysisUpdate.getDescription());
        when(analysisRepository.save(mock)).thenAnswer(invocation -> invocation.getArgument(0));

        assertAll(
                () -> assertDoesNotThrow(() -> service.update(mock.getId(), mockAnalysisUpdate)),
                () -> verify(analysisRepository, times(1)).findById(mock.getId()),
                () -> verify(analysisRepository, times(1)).save(mock)
        );
    }

    @Test
    @DisplayName("#delete > When the analysis is not found > Throw an exception")
    void deleteWhenTheAnalysisIsNotFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(analysisRepository.findById(mockAnalysisId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(mockAnalysisId));
    }

    @Test
    @DisplayName("#delete > When the analysis is found > Delete the analysis")
    void deleteWhenTheAnalysisIsFoundDeleteTheAnalysis() {
        Analysis mock = assembleAnalysis.get();

        when(analysisRepository.findById(mock.getId())).thenReturn(Optional.of(mock));

        assertAll(
                () -> assertDoesNotThrow(() -> service.delete(mock.getId())),
                () -> verify(analysisRepository, times(1)).findById(mock.getId()),
                () -> verify(analysisRepository, times(1)).deleteById(mock.getId())
        );
    }

    private final Supplier<Analysis> assembleAnalysis = () -> Analysis.builder()
            .id(UUID.randomUUID())
            .name("Mock analysis")
            .user(User.builder().id(UUID.randomUUID()).build())
            .createdAt(Instant.now())
            .build();

    private final Supplier<AnalysisUpdateDto> assembleAnalysisUpdate = () -> AnalysisUpdateDto.builder()
            .name("New name")
            .description("New description")
            .build();

    private final Supplier<AnalysisInsertDto> assembleAnalysisInsert = () -> AnalysisInsertDto.builder()
            .name("New analysis")
            .description("New description")
            .userId(UUID.randomUUID())
            .build();

}
