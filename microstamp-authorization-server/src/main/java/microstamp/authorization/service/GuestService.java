package microstamp.authorization.service;

import microstamp.authorization.dto.ExportReadDto;

import java.util.List;
import java.util.UUID;

public interface GuestService {

    List<ExportReadDto> findAll();

    ExportReadDto findById(UUID analysisId);

}