package microstamp.step1.unit;

import feign.FeignException;
import microstamp.step1.client.MicroStampClient;
import microstamp.step1.dto.hazard.HazardInsertDto;
import microstamp.step1.dto.hazard.HazardReadDto;
import microstamp.step1.dto.hazard.HazardUpdateDto;
import microstamp.step1.entity.Hazard;
import microstamp.step1.entity.Loss;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.exception.Step1OrphanException;
import microstamp.step1.exception.Step1SelfParentingHazardException;
import microstamp.step1.repository.HazardRepository;
import microstamp.step1.repository.LossRepository;
import microstamp.step1.service.impl.HazardServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HazardServiceImplUnitTest {

    @InjectMocks
    private HazardServiceImpl service;

    @Mock
    private HazardRepository hazardRepository;

    @Mock
    private LossRepository lossRepository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no hazard is found > Return an empty list")
    void findAllWhenNoHazardIsFoundReturnAnEmptyList() {
        when(hazardRepository.findAll()).thenReturn(List.of());

        List<HazardReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are hazards found > Return the hazards")
    void findAllWhenThereAreHazardsFoundReturnTheHazards() {
        when(hazardRepository.findAll()).thenReturn(List.of(assembleHazard.apply(1), assembleHazard.apply(2)));

        List<HazardReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals("Hazard 1", response.getFirst().getName()),
                () -> assertEquals("H-1", response.getFirst().getCode()),
                () -> assertEquals("Hazard 2", response.get(1).getName()),
                () -> assertEquals("H-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no hazard is found > Throw an exception")
    void findByIdWhenNoHazardIsFoundThrowAnException() {
        UUID mockHazardId = UUID.randomUUID();
        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockHazardId));
    }

    @Test
    @DisplayName("#findById > When the hazard is found > Return the hazard")
    void findByIdWhenTheHazardIsFoundReturnTheHazard() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(1);
        mock.setId(mockHazardId);
        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));

        HazardReadDto response = service.findById(mockHazardId);
        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getCode(), response.getCode())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no hazard is found > Return an empty list")
    void findByAnalysisIdWhenNoHazardIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(hazardRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<HazardReadDto> hazards = service.findByAnalysisId(mockAnalysisId);

        assertTrue(hazards.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are hazards found > Return the hazards")
    void findByAnalysisIdWhenThereAreHazardsFoundReturnTheHazards() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(hazardRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleHazard.apply(1), assembleHazard.apply(2)
        ));

        List<HazardReadDto> hazards = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(hazards.isEmpty()),
                () -> Assertions.assertEquals(2, hazards.size()),
                () -> Assertions.assertEquals("Hazard 1", hazards.getFirst().getName()),
                () -> Assertions.assertEquals("H-1", hazards.getFirst().getCode()),
                () -> Assertions.assertEquals("Hazard 2", hazards.get(1).getName()),
                () -> Assertions.assertEquals("H-2", hazards.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When the input is invalid > Throw an exception")
    void insertWhenTheInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When there is an error when finding the analysis > Throw an exception")
    void insertWhenThereIsAnErrorWhenFindingTheAnalysisThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();
        HazardInsertDto mock = assembleHazardInsertDto.get();
        mock.setAnalysisId(mockAnalysisId);

        when(microStampClient.getAnalysisById(mockAnalysisId)).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> service.insert(mock));
    }

    @Test
    @DisplayName("#insert > When a loss associated with the hazard is not found > Throw an exception")
    void insertWhenALossAssociatedWithTheHazardIsNotFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();
        HazardInsertDto mock = assembleHazardInsertDto.get();
        mock.setAnalysisId(mockAnalysisId);

        when(microStampClient.getAnalysisById(mockAnalysisId)).thenReturn(null);
        when(lossRepository.findById(mock.getLossIds().getFirst())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.insert(mock));
    }

    @Test
    @DisplayName("#insert > When it is a children hazard > When the father is not found > Throw an exception")
    void insertWhenItIsAChildrenHazardWhenTheFatherIsNotFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();
        HazardInsertDto mock = assembleHazardInsertDto.get();
        mock.setAnalysisId(mockAnalysisId);

        when(microStampClient.getAnalysisById(mockAnalysisId)).thenReturn(null);
        when(lossRepository.findById(mock.getLossIds().getFirst())).thenReturn(Optional.of(assembleLoss.apply(mock.getLossIds().getFirst(), 1)));
        when(lossRepository.findById(mock.getLossIds().get(1))).thenReturn(Optional.of(assembleLoss.apply(mock.getLossIds().get(1), 2)));
        when(hazardRepository.findById(mock.getFatherId())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.insert(mock));
    }

    @Test
    @DisplayName("#insert > When it is a children hazard > When the father is found > Save the children hazard")
    void insertWhenItIsAChildrenHazardWhenTheFatherIsFoundSaveTheChildrenHazard() {
        UUID mockAnalysisId = UUID.randomUUID();
        HazardInsertDto mock = assembleHazardInsertDto.get();
        mock.setAnalysisId(mockAnalysisId);
        Hazard mockFather = assembleHazard.apply(1);
        mockFather.setId(mock.getFatherId());

        when(microStampClient.getAnalysisById(mockAnalysisId)).thenReturn(null);
        when(lossRepository.findById(mock.getLossIds().getFirst())).thenReturn(Optional.of(assembleLoss.apply(mock.getLossIds().getFirst(), 1)));
        when(lossRepository.findById(mock.getLossIds().get(1))).thenReturn(Optional.of(assembleLoss.apply(mock.getLossIds().get(1), 2)));
        when(hazardRepository.findById(mock.getFatherId())).thenReturn(Optional.of(mockFather));

        HazardReadDto response = service.insert(mock);

        assertAll(
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getCode(), response.getCode()),
                () -> assertEquals(mock.getFatherId(), response.getFather().getId()),
                () -> assertEquals(mock.getLossIds().getFirst(), response.getLosses().getFirst().getId()),
                () -> assertEquals(mock.getLossIds().get(1), response.getLosses().get(1).getId())
        );
    }


    @Test
    @DisplayName("#update > When no hazard is found > Throw an exception")
    void updateWhenNoHazardIsFoundThrowAnException() {
        UUID mockHazardId = UUID.randomUUID();

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockHazardId, assembleHazardUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When no associated loss is found > Throw an exception")
    void updateWhenNoAssociatedLossIsFoundThrowAnException() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(1);
        HazardUpdateDto hazardUpdateDto = assembleHazardUpdateDto.get();

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().getFirst())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockHazardId, hazardUpdateDto));
    }

    @Test
    @DisplayName("#update > When the hazard has a forbidden connection with itself > Throw an exception")
    void updateWhenTheHazardHasAForbiddenConnectionWithItselfThrowAnException() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(1);
        HazardUpdateDto hazardUpdateDto = assembleHazardUpdateDto.get();
        hazardUpdateDto.setFatherId(mockHazardId);

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().getFirst())).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 1)));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().get(1))).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 2)));

        assertThrows(Step1SelfParentingHazardException.class, () -> service.update(mockHazardId, hazardUpdateDto));
    }

    @Test
    @DisplayName("#update > When the father of the updated hazard is not found > Throw an exception")
    void updateWhenTheFatherOfTheUpdatedHazardIsNotFoundThrowAnException() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(1);
        HazardUpdateDto hazardUpdateDto = assembleHazardUpdateDto.get();

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().getFirst())).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 1)));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().get(1))).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 2)));
        when(hazardRepository.findById(hazardUpdateDto.getFatherId())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockHazardId, hazardUpdateDto));
    }

    @Test
    @DisplayName("#update > When the hazard has its own father as child > Throw an exception")
    void updateWhenTheHazardHasItsOwnFatherAsChildThrowAnException() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(2);
        mock.setId(mockHazardId);
        HazardUpdateDto hazardUpdateDto = assembleHazardUpdateDto.get();
        Hazard mockChild = assembleHazard.apply(3);
        mockChild.setId(hazardUpdateDto.getFatherId());

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().getFirst())).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 1)));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().get(1))).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 2)));
        when(hazardRepository.findById(hazardUpdateDto.getFatherId())).thenReturn(Optional.of(assembleHazard.apply(1)));
        when(hazardRepository.findHazardChildren(mockHazardId.toString())).thenReturn(List.of(mockChild));
        when(hazardRepository.findHazardChildren(mockChild.getId().toString())).thenReturn(List.of());

        assertThrows(Step1OrphanException.class, () -> service.update(mockHazardId, hazardUpdateDto));
    }

    @Test
    @DisplayName("#update > When the father is not null > Update the father")
    void updateWhenTheFatherIsNotNullUpdateTheFather() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(2);
        mock.setId(mockHazardId);
        HazardUpdateDto hazardUpdateDto = assembleHazardUpdateDto.get();
        Hazard mockChild = assembleHazard.apply(3);

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().getFirst())).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 1)));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().get(1))).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 2)));
        when(hazardRepository.findById(hazardUpdateDto.getFatherId())).thenReturn(Optional.of(assembleHazard.apply(1)));
        when(hazardRepository.findHazardChildren(mockHazardId.toString())).thenReturn(List.of(mockChild));
        when(hazardRepository.findHazardChildren(mockChild.getId().toString())).thenReturn(List.of());

        assertAll(
                () -> assertDoesNotThrow(() -> service.update(mockHazardId, hazardUpdateDto)),
                () -> verify(hazardRepository, times(1)).save(any(Hazard.class))
        );
    }

    @Test
    @DisplayName("#update > When the father is null > Update the hazard")
    void updateWhenTheFatherIsNullUpdateTheHazard() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(2);
        mock.setId(mockHazardId);
        HazardUpdateDto hazardUpdateDto = assembleHazardUpdateDto.get();
        hazardUpdateDto.setFatherId(null);

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().getFirst())).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 1)));
        when(lossRepository.findById(hazardUpdateDto.getLossIds().get(1))).thenReturn(Optional.of(assembleLoss.apply(mockHazardId, 2)));

        assertAll(
                () -> assertDoesNotThrow(() -> service.update(mockHazardId, hazardUpdateDto)),
                () -> verify(hazardRepository, times(1)).save(any(Hazard.class))
        );
    }


    @Test
    @DisplayName("#delete > When the hazard does not have any children > Delete the hazard")
    void deleteWhenTheHazardDoesNotHaveAnyChildrenDeleteTheHazard() {
        UUID mockHazardId = UUID.randomUUID();

        when(hazardRepository.findHazardChildren(mockHazardId.toString())).thenReturn(Collections.emptyList());

        service.delete(mockHazardId);

        assertAll(
                () -> verify(hazardRepository, times(1)).deleteLossesAssociation(mockHazardId.toString()),
                () -> verify(hazardRepository, times(1)).deleteSystemSafetyConstraintsAssociation(mockHazardId.toString()),
                () -> verify(hazardRepository, times(1)).deleteById(mockHazardId)
        );
    }

    @Test
    @DisplayName("#delete > When the hazard has children > Delete the hazard and its children")
    void deleteWhenTheHazardHasChildrenDeleteTheHazardAndItsChildren() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(1);
        mock.setId(mockHazardId);

        when(hazardRepository.findHazardChildren(mockHazardId.toString())).thenReturn(List.of(mock));
        when(hazardRepository.findHazardChildren(mockHazardId.toString())).thenReturn(Collections.emptyList());

        service.delete(mockHazardId);

        assertAll(
                () -> verify(hazardRepository, times(1)).deleteLossesAssociation(mockHazardId.toString()),
                () -> verify(hazardRepository, times(1)).deleteSystemSafetyConstraintsAssociation(mockHazardId.toString()),
                () -> verify(hazardRepository, times(1)).deleteById(mockHazardId),
                () -> verify(hazardRepository, times(1)).deleteLossesAssociation(mock.getId().toString()),
                () -> verify(hazardRepository, times(1)).deleteSystemSafetyConstraintsAssociation(mock.getId().toString()),
                () -> verify(hazardRepository, times(1)).deleteById(mock.getId())
        );
    }

    @Test
    @DisplayName("#getHazardChildren > When no hazard is found > Throw an exception")
    void getHazardChildrenWhenNoHazardIsFoundThrowAnException() {
        UUID mockHazardId = UUID.randomUUID();

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.getHazardChildren(mockHazardId));
    }

    @Test
    @DisplayName("#getHazardChildren > When the hazard is found > When the hazard does not have any children > Return an empty list")
    void getHazardChildrenWhenTheHazardIsFoundWhenTheHazardDoesNotHaveAnyChildrenReturnAnEmptyList() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(1);
        mock.setId(mockHazardId);

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));
        when(hazardRepository.findHazardChildren(mockHazardId.toString())).thenReturn(List.of());

        List<HazardReadDto> response = service.getHazardChildren(mockHazardId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#getHazardChildren > When the hazard is found > When the hazard have children > Return the children")
    void getHazardChildrenWhenTheHazardIsFoundWhenTheHazardHaveChildrenReturnTheChildren() {
        UUID mockHazardId = UUID.randomUUID();
        Hazard mock = assembleHazard.apply(1);
        mock.setId(mockHazardId);
        Hazard mockChild = assembleHazard.apply(2);

        when(hazardRepository.findById(mockHazardId)).thenReturn(Optional.of(mock));
        when(hazardRepository.findHazardChildren(mockHazardId.toString())).thenReturn(List.of(mockChild));
        when(hazardRepository.findHazardChildren(mockChild.getId().toString())).thenReturn(List.of());

        List<HazardReadDto> response = service.getHazardChildren(mockHazardId);

        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals(mockChild.getId(), response.getFirst().getId()),
                () -> assertEquals(mockChild.getName(), response.getFirst().getName()),
                () -> assertEquals(mockChild.getCode(), response.getFirst().getCode())
        );
    }


    private final Function<Integer, Hazard> assembleHazard = (index) -> Hazard.builder()
            .id(UUID.randomUUID())
            .name("Hazard " + index)
            .code("H-" + index)
            .build();

    private final Supplier<HazardInsertDto> assembleHazardInsertDto = () -> HazardInsertDto.builder()
            .lossIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
            .fatherId(UUID.randomUUID())
            .analysisId(UUID.randomUUID())
            .code("H-2")
            .name("Hazard 2")
            .build();

    private final BiFunction<UUID, Integer, Loss> assembleLoss = (uuid, index) -> Loss.builder()
            .id(uuid)
            .name("Name " + index)
            .code("H-" + index)
            .build();

    private final Supplier<HazardUpdateDto> assembleHazardUpdateDto = () -> HazardUpdateDto.builder()
            .lossIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
            .name("Updated Hazard")
            .code("UH-1")
            .fatherId(UUID.randomUUID())
            .build();


}
