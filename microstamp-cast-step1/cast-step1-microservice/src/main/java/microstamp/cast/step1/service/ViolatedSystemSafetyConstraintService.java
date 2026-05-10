package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintInsertDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ViolatedSystemSafetyConstraintService {

    List<ViolatedSystemSafetyConstraintReadDto> findAll();

    ViolatedSystemSafetyConstraintReadDto findById(UUID id);

    List<ViolatedSystemSafetyConstraintReadDto> findByAnalysisId(UUID id);

    ViolatedSystemSafetyConstraintReadDto insert(ViolatedSystemSafetyConstraintInsertDto violatedSystemSafetyConstraintInsertDto);

    void update(UUID id, ViolatedSystemSafetyConstraintUpdateDto violatedSystemSafetyConstraintUpdateDto);

    void delete(UUID id);
}