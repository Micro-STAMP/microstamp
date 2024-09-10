package microstamp.authorization.service;

import microstamp.authorization.dto.ExportReadDto;

import java.io.IOException;
import java.util.UUID;

public interface ExportService {

    /**
     * Export the content of an analysis by its UUID in JSON format.
     *
     * @param analysisId The UUID of the analysis.
     * @return ExportReadDto containing the analysis.
     */
    ExportReadDto exportToJson(UUID analysisId);

    /**
     * Export the content of an analysis by its UUID in PDF format.
     *
     * @param analysisId The UUID of the analysis.
     * @return byte[] containing the PDF representation of analysis.
     */
    byte[] exportToPdf(UUID analysisId) throws IOException;

}
