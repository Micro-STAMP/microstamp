package step3.unit;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import step3.dto.mit.mapper.SafetyConstraintMapper;
import step3.dto.mit.safety_constraint.SafetyConstraintReadDto;
import step3.entity.mit.SafetyConstraint;
import step3.entity.mit.UCAType;
import step3.entity.mit.UnsafeControlAction;
import step3.repository.mit.SafetyConstraintRepository;
import step3.service.mit.SafetyConstraintService;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SafetyConstraintServiceUnitTest {

    @InjectMocks
    private SafetyConstraintService service;

    @Mock
    private SafetyConstraintMapper mapper;

    @Mock
    private SafetyConstraintRepository safetyConstraintRepository;

    @Test
    @DisplayName("#readSafetyConstraint > When no safet constraint is found > Throw an exception")
    void readSafetyConstraintWhenNoSafetConstraintIsFoundThrowAnException() {
        UUID safetyConstraintId = UUID.randomUUID();

        when(safetyConstraintRepository.getReferenceById(safetyConstraintId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.readSafetyConstraint(safetyConstraintId));
    }

    @Test
    @DisplayName("#readSafetyConstraint > When the safety constraint is found > Return the safety constraint")
    void readSafetyConstraintWhenTheSafetyConstraintIsFoundReturnTheSafetyConstraint() {
        SafetyConstraint mockSafetyConstraint = assembleSafetyConstraint.get();
        SafetyConstraintReadDto mockSafetyConstraintRead = assembleSafetyConstraintRead.apply(mockSafetyConstraint.getId(), mockSafetyConstraint.getUnsafeControlAction().getId());

        when(safetyConstraintRepository.getReferenceById(mockSafetyConstraint.getId())).thenReturn(mockSafetyConstraint);
        when(mapper.toSafetyConstraintReadDto(mockSafetyConstraint)).thenReturn(mockSafetyConstraintRead);

        assertEquals(mockSafetyConstraintRead, service.readSafetyConstraint(mockSafetyConstraint.getId()));
    }

    @Test
    @DisplayName("#readSafetyConstraintByUCAId > When no safet constraint is found > Throw an exception")
    void readSafetyConstraintByUCAIdWhenNoSafetConstraintIsFoundThrowAnException() {
        UUID unsafeControlActionId = UUID.randomUUID();

        when(safetyConstraintRepository.findByUnsafeControlActionId(unsafeControlActionId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.readSafetyConstraintByUCAId(unsafeControlActionId));
    }

    @Test
    @DisplayName("#readSafetyConstraintByUCAId > When the safety constraint is found > Return the safety constraint")
    void readSafetyConstraintByUCAIdWhenTheSafetyConstraintIsFoundReturnTheSafetyConstraint() {
        SafetyConstraint mockSafetyConstraint = assembleSafetyConstraint.get();
        SafetyConstraintReadDto mockSafetyConstraintRead = assembleSafetyConstraintRead.apply(mockSafetyConstraint.getId(), mockSafetyConstraint.getUnsafeControlAction().getId());

        when(safetyConstraintRepository.findByUnsafeControlActionId(mockSafetyConstraint.getUnsafeControlAction().getId())).thenReturn(mockSafetyConstraint);
        when(mapper.toSafetyConstraintReadDto(mockSafetyConstraint)).thenReturn(mockSafetyConstraintRead);

        assertEquals(mockSafetyConstraintRead, service.readSafetyConstraintByUCAId(mockSafetyConstraint.getUnsafeControlAction().getId()));
    }


    @Test
    @DisplayName("#readAllSafetyConstraints > When no safety constraint are found > Return an empty list")
    void readAllSafetyConstraintsWhenNoSafetyConstraintAreFoundReturnAnEmptyList() {
        when(safetyConstraintRepository.findAll()).thenReturn(List.of());

        assertTrue(safetyConstraintRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("#readAllSafetyConstraints > When the safety constraints are found > Return the list containing the safety constraints")
    void readAllSafetyConstraintsWhenTheSafetyConstraintsAreFoundReturnTheListContainingTheSafetyConstraints() {
        SafetyConstraint mockFirst = assembleSafetyConstraint.get();
        SafetyConstraint mockSecond = assembleSafetyConstraint.get();
        SafetyConstraintReadDto mockFirstRead = assembleSafetyConstraintRead.apply(mockFirst.getId(), mockFirst.getUnsafeControlAction().getId());
        SafetyConstraintReadDto mockSecondRead = assembleSafetyConstraintRead.apply(mockSecond.getId(), mockSecond.getUnsafeControlAction().getId());

        when(safetyConstraintRepository.findAll()).thenReturn(List.of(mockFirst, mockSecond));
        when(mapper.toSafetyConstraintReadDto(mockFirst)).thenReturn(mockFirstRead);
        when(mapper.toSafetyConstraintReadDto(mockSecond)).thenReturn(mockSecondRead);

        List<SafetyConstraintReadDto> response = service.readAllSafetyConstraints();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(mockFirstRead, response.getFirst()),
                () -> assertEquals(mockSecondRead, response.getLast())
        );
    }

    private final Supplier<SafetyConstraint> assembleSafetyConstraint = () -> SafetyConstraint.builder()
            .id(UUID.randomUUID())
            .name("Name")
            .unsafeControlAction(UnsafeControlAction.builder()
                    .id(UUID.randomUUID())
                    .name("UCA")
                    .controllerId(UUID.randomUUID())
                    .controlActionId(UUID.randomUUID())
                    .hazardId(UUID.randomUUID())
                    .type(UCAType.PROVIDED)
                    .analysisId(UUID.randomUUID())
                    .ruleCode("Rule 01")
                    .build())
            .build();

    private final BiFunction<UUID, UUID, SafetyConstraintReadDto> assembleSafetyConstraintRead = (id, ucaId) -> SafetyConstraintReadDto.builder()
            .id(id)
            .name("Name")
            .uca_id(ucaId)
            .build();

}
