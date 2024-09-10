package microstamp.step1.service;

import microstamp.step1.dto.export.ExportDto;

import java.io.IOException;
import java.util.UUID;

public interface ExportService {

    /**
     * Export the Step 1 content of an analysis by its UUID in JSON format.
     *
     * @param analysisId The UUID of the analysis.
     * @return ExportDto containing the Step 1 content of the analysis.
     */
    ExportDto exportToJson(UUID analysisId);

    /**
     * Export the Step 1 content of an analysis by its UUID in PDF format.
     *
     * @param analysisId The UUID of the analysis.
     * @return byte[] containing the PDF representation of the Step 1 content.
     */
    byte[] exportToPdf(UUID analysisId) throws IOException;

}
