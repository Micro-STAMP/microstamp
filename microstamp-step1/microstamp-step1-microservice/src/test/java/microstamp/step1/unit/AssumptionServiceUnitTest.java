package microstamp.step1.unit;

import microstamp.step1.client.MicroStampClient;
import microstamp.step1.dto.assumption.AssumptionInsertDto;
import microstamp.step1.dto.assumption.AssumptionReadDto;
import microstamp.step1.dto.assumption.AssumptionUpdateDto;
import microstamp.step1.entity.Assumption;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.repository.AssumptionRepository;
import microstamp.step1.service.impl.AssumptionServiceImpl;
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
public class AssumptionServiceUnitTest {

    @InjectMocks
    private AssumptionServiceImpl service;

    @Mock
    private AssumptionRepository assumptionRepository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no assumption is found > Return an empty list")
    void findAllWhenNoAssumptionIsFoundReturnAnEmptyList() {
        when(assumptionRepository.findAll()).thenReturn(List.of());
        List<AssumptionReadDto> assumptions = service.findAll();

        assertTrue(assumptions.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are assumptions found > Return the assumptions")
    void findAllWhenThereAreAssumptionsFoundReturnTheAssumptions() {
        when(assumptionRepository.findAll()).thenReturn(List.of(
                assembleAssumption.apply(1), assembleAssumption.apply(2)
        ));
        List<AssumptionReadDto> assumptions = service.findAll();

        assertAll(
                () -> Assertions.assertFalse(assumptions.isEmpty()),
                () -> Assertions.assertEquals(2, assumptions.size()),
                () -> Assertions.assertEquals("Name 1", assumptions.getFirst().getName()),
                () -> Assertions.assertEquals("Code 1", assumptions.getFirst().getCode()),
                () -> Assertions.assertEquals("Name 2", assumptions.get(1).getName()),
                () -> Assertions.assertEquals("Code 2", assumptions.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no assumption is found > Throw an exception")
    void findByIdWhenNoAssumptionIsFoundThrowAnException() {
        UUID mockAssumptionId = UUID.randomUUID();
        when(assumptionRepository.findById(mockAssumptionId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockAssumptionId));
    }

    @Test
    @DisplayName("#findById > When the assumption is found > Return the assumption")
    void findByIdWhenTheAssumptionIsFoundReturnTheAssumption() {
        UUID mockAssumptionId = UUID.randomUUID();
        Assumption mock = assembleAssumption.apply(1);
        mock.setId(mockAssumptionId);
        when(assumptionRepository.findById(mockAssumptionId)).thenReturn(Optional.of(mock));

        AssumptionReadDto response = service.findById(mockAssumptionId);
        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getCode(), response.getCode())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no assumption is found > Return an empty list")
    void findByAnalysisIdWhenNoAssumptionIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(assumptionRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<AssumptionReadDto> assumptions = service.findByAnalysisId(mockAnalysisId);

        assertTrue(assumptions.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are assumptions found > Return the assumptions")
    void findByAnalysisIdWhenThereAreAssumptionsFoundReturnTheAssumptions() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(assumptionRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleAssumption.apply(1), assembleAssumption.apply(2)
        ));

        List<AssumptionReadDto> assumptions = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(assumptions.isEmpty()),
                () -> Assertions.assertEquals(2, assumptions.size()),
                () -> Assertions.assertEquals("Name 1", assumptions.getFirst().getName()),
                () -> Assertions.assertEquals("Code 1", assumptions.getFirst().getCode()),
                () -> Assertions.assertEquals("Name 2", assumptions.get(1).getName()),
                () -> Assertions.assertEquals("Code 2", assumptions.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When the input is invalid > Throw an exception")
    void insertWhenTheInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When the input are valid > Create the assumption")
    void insertWhenTheInputAreValidCreateTheAssumption() {
        AssumptionInsertDto mockAssumptionInsertDto = assembleAssumptionInsertDto.get();

        when(microStampClient.getAnalysisById(mockAssumptionInsertDto.getAnalysisId())).thenReturn(null);
        when(assumptionRepository.save(any(Assumption.class))).thenAnswer(answer -> answer.getArguments()[0]);

        AssumptionReadDto response = service.insert(mockAssumptionInsertDto);
        assertAll(
                () -> assertEquals(mockAssumptionInsertDto.getName(), response.getName()),
                () -> assertEquals(mockAssumptionInsertDto.getCode(), response.getCode())
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
        UUID mockAssumptionId = UUID.randomUUID();
        when(assumptionRepository.findById(mockAssumptionId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockAssumptionId, assembleAssumptionUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When the analysis exist > Update the assumption")
    void updateWhenTheAnalysisExistUpdateTheAssumption() {
        UUID mockAssumptionId = UUID.randomUUID();
        Assumption mockAssumption = assembleAssumption.apply(1);
        AssumptionUpdateDto mockAssumptionUpdate = assembleAssumptionUpdateDto.get();
        when(assumptionRepository.findById(mockAssumptionId)).thenReturn(Optional.of(mockAssumption));
        mockAssumption.setName(mockAssumptionUpdate.getName());

        service.update(mockAssumptionId, mockAssumptionUpdate);

        assertAll(
                () -> verify(assumptionRepository, times(1)).findById(mockAssumptionId),
                () -> verify(assumptionRepository, times(1)).save(mockAssumption)
        );
    }

    @Test
    @DisplayName("#delete > When no assumption is found > Throw an exception")
    void deleteWhenNoAssumptionIsFoundThrowAnException() {
        UUID mockAssumptionId = UUID.randomUUID();
        when(assumptionRepository.findById(mockAssumptionId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockAssumptionId));
    }

    @Test
    @DisplayName("#delete > When the assumption is found > Delete the assumption")
    void deleteWhenTheAssumptionIsFoundDeleteTheAssumption() {
        UUID mockAssumptionId = UUID.randomUUID();
        Assumption mockAssumption = assembleAssumption.apply(1);
        mockAssumption.setId(mockAssumptionId);
        when(assumptionRepository.findById(mockAssumptionId)).thenReturn(Optional.of(mockAssumption));

        service.delete(mockAssumptionId);

        assertAll(
                () -> verify(assumptionRepository, times(1)).findById(mockAssumptionId),
                () -> verify(assumptionRepository, times(1)).deleteById(mockAssumptionId)
        );
    }

    private final Function<Integer, Assumption> assembleAssumption = (index) -> Assumption.builder()
            .id(UUID.randomUUID())
            .name("Name " + index)
            .code("Code " + index)
            .build();

    private final Supplier<AssumptionInsertDto> assembleAssumptionInsertDto = () -> AssumptionInsertDto.builder()
            .name("Insert")
            .code("Code")
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<AssumptionUpdateDto> assembleAssumptionUpdateDto = () -> AssumptionUpdateDto.builder()
            .name("Insert")
            .code("Code")
            .build();


}
