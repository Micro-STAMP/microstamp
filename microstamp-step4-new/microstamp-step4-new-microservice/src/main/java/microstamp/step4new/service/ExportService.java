package microstamp.step4new.service;

import microstamp.step4new.dto.export.ExportReadDto;

import java.io.IOException;
import java.util.UUID;

public interface ExportService {

    /**
     * Export the Step 4 New content of an analysis by its UUID in JSON format.
     *
     * @param analysisId The UUID of the analysis.
     * @return ExportReadDto containing the Step 4 New content of the analysis.
     */
    ExportReadDto exportToJson(UUID analysisId);

    /**
     * Export the Step 4 New content of an analysis by its UUID in PDF format.
     *
     * @param analysisId The UUID of the analysis.
     * @return byte[] containing the PDF representation of the Step 4 New content.
     */
    byte[] exportToPdf(UUID analysisId) throws IOException;

}
