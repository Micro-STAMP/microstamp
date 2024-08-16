package microstamp.step1.unit;

import microstamp.step1.client.MicroStampClient;
import microstamp.step1.dto.loss.LossInsertDto;
import microstamp.step1.dto.loss.LossReadDto;
import microstamp.step1.dto.loss.LossUpdateDto;
import microstamp.step1.entity.Loss;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.repository.LossRepository;
import microstamp.step1.service.impl.LossServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LossServiceUnitTest {

    @InjectMocks
    private LossServiceImpl service;

    @Mock
    private LossRepository repository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no loss is found > Return an empty list")
    void findAllWhenNoLossIsFoundReturnAnEmptyList() {
        when(repository.findAll()).thenReturn(List.of());
        List<LossReadDto> losses = service.findAll();

        assertTrue(losses.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are losses found > Return the losses")
    void findAllWhenThereAreLossesFoundReturnTheAssumptions() {
        when(repository.findAll()).thenReturn(List.of(
                assembleLoss.apply(1), assembleLoss.apply(2)
        ));
        List<LossReadDto> losses = service.findAll();

        assertAll(
                () -> Assertions.assertFalse(losses.isEmpty()),
                () -> Assertions.assertEquals(2, losses.size()),
                () -> Assertions.assertEquals("Name 1", losses.getFirst().getName()),
                () -> Assertions.assertEquals("Code 1", losses.getFirst().getCode()),
                () -> Assertions.assertEquals("Name 2", losses.get(1).getName()),
                () -> Assertions.assertEquals("Code 2", losses.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no loss is found > Throw an exception")
    void findByIdWhenNoLossIsFoundThrowAnException() {
        UUID mockLossId = UUID.randomUUID();
        when(repository.findById(mockLossId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockLossId));
    }

    @Test
    @DisplayName("#findById > When the loss is found > Return the loss")
    void findByIdWhenTheLossIsFoundReturnTheLoss() {
        UUID mockLossId = UUID.randomUUID();
        Loss mock = assembleLoss.apply(1);
        mock.setId(mockLossId);
        when(repository.findById(mockLossId)).thenReturn(Optional.of(mock));

        LossReadDto response = service.findById(mockLossId);
        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getCode(), response.getCode())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no loss is found > Return an empty list")
    void findByAnalysisIdWhenNoLossIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(repository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<LossReadDto> losses = service.findByAnalysisId(mockAnalysisId);

        assertTrue(losses.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are losses found > Return the losses")
    void findByAnalysisIdWhenThereAreLossesFoundReturnTheLosses() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(repository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleLoss.apply(1), assembleLoss.apply(2)
        ));

        List<LossReadDto> losses = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(losses.isEmpty()),
                () -> Assertions.assertEquals(2, losses.size()),
                () -> Assertions.assertEquals("Name 1", losses.getFirst().getName()),
                () -> Assertions.assertEquals("Code 1", losses.getFirst().getCode()),
                () -> Assertions.assertEquals("Name 2", losses.get(1).getName()),
                () -> Assertions.assertEquals("Code 2", losses.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When the input is invalid > Throw an exception")
    void insertWhenTheInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When the input are valid > Create the loss")
    void insertWhenTheInputAreValidCreateTheLoss() {
        LossInsertDto mockLossInsertDto = assembleLossInsertDto.get();

        when(microStampClient.getAnalysisById(mockLossInsertDto.getAnalysisId())).thenReturn(null);
        when(repository.save(any(Loss.class))).thenAnswer(answer -> answer.getArguments()[0]);

        LossReadDto response = service.insert(mockLossInsertDto);
        assertAll(
                () -> assertEquals(mockLossInsertDto.getName(), response.getName()),
                () -> assertEquals(mockLossInsertDto.getCode(), response.getCode())
        );
    }

    @Test
    @DisplayName("#update > When the input is invalid > Throw an exception")
    void updateWhenTheInputIsInvalidThrowAnException() {
        assertAll(
                () -> assertThrows(Step1IllegalArgumentException.class, () -> service.update(null, null)),
                () -> assertThrows(Step1IllegalArgumentException.class, () -> service.update(UUID.randomUUID(), null))
        );
    }

    @Test
    @DisplayName("#update > When the analysis does not exist > Throw an exception")
    void updateWhenTheAnalysisDoesNotExistThrowAnException() {
        UUID mockLossId = UUID.randomUUID();
        when(repository.findById(mockLossId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockLossId, assembleLossUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When the analysis exist > Update the loss")
    void updateWhenTheAnalysisExistUpdateTheLoss() {
        UUID mockLossId = UUID.randomUUID();
        Loss mockLoss = assembleLoss.apply(1);
        LossUpdateDto mockLossUpdate = assembleLossUpdateDto.get();
        when(repository.findById(mockLossId)).thenReturn(Optional.of(mockLoss));
        mockLoss.setName(mockLossUpdate.getName());

        service.update(mockLossId, mockLossUpdate);

        assertAll(
                () -> verify(repository, times(1)).findById(mockLossId),
                () -> verify(repository, times(1)).save(mockLoss)
        );
    }

    @Test
    @DisplayName("#delete > When no loss is found > Throw an exception")
    void deleteWhenNoLossIsFoundThrowAnException() {
        UUID mockLossId = UUID.randomUUID();
        when(repository.findById(mockLossId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockLossId));
    }

    @Test
    @DisplayName("#delete > When the loss is found > Delete the loss and hazards")
    void deleteWhenTheLossIsFoundDeleteTheLossAndHazards() {
        UUID mockLossId = UUID.randomUUID();
        Loss mockLoss = assembleLoss.apply(1);
        mockLoss.setId(mockLossId);
        when(repository.findById(mockLossId)).thenReturn(Optional.of(mockLoss));

        service.delete(mockLossId);

        assertAll(
                () -> verify(repository, times(1)).findById(mockLossId),
                () -> verify(repository, times(1)).deleteById(mockLossId)
        );
    }

    private final Function<Integer, Loss> assembleLoss = (index) -> Loss.builder()
            .id(UUID.randomUUID())
            .name("Name " + index)
            .code("Code " + index)
            .build();

    private final Supplier<LossInsertDto> assembleLossInsertDto = () -> LossInsertDto.builder()
            .name("Insert")
            .code("Code")
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<LossUpdateDto> assembleLossUpdateDto = () -> LossUpdateDto.builder()
            .name("Insert")
            .code("Code")
            .build();
}
