package microstamp.step2.service.impl;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step2.dto.component.ComponentReadDto;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.connectionaction.ConnectionActionReadDto;
import microstamp.step2.dto.export.ExportReadDto;
import microstamp.step2.dto.image.ImageReadDto;
import microstamp.step2.dto.responsibility.ResponsibilityReadDto;
import microstamp.step2.dto.state.StateReadDto;
import microstamp.step2.dto.variable.VariableReadDto;
import microstamp.step2.service.ComponentService;
import microstamp.step2.service.ConnectionService;
import microstamp.step2.service.ExportService;
import microstamp.step2.service.ImageService;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final ComponentService componentService;

    private final ConnectionService connectionService;

    private final ImageService imageService;

    public ExportReadDto exportToJson(UUID analysisId) {
        log.info("Exporting (JSON) Step 2 content of an analysis by its UUID: {}", analysisId);
        return getExportDto(analysisId);
    }

    public byte[] exportToPdf(UUID analysisId) throws IOException {
        log.info("Exporting (PDF) Step 2 content of an analysis by its UUID: {}", analysisId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        ExportReadDto exportReadDto = getExportDto(analysisId);

        setTitle(document);
        setAnalysisSection(document, exportReadDto.getAnalysisId());
        setComponentsSection(document, exportReadDto.getComponents());
        setConnectionsSection(document, exportReadDto.getConnections());
        setImagesSection(document, exportReadDto.getImages());

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private ExportReadDto getExportDto(UUID analysisId) {
        return ExportReadDto.builder()
                .analysisId(analysisId)
                .components(componentService.findByAnalysisId(analysisId))
                .connections(connectionService.findByAnalysisId(analysisId))
                .images(imageService.findByAnalysisId(analysisId))
                .build();
    }

    private void setTitle(Document document) throws IOException {
        Style style = new Style();
        style.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC));

        document.add(new Paragraph("MICROSTAMP ANALYSIS - STEP 2")
                .setFontSize(20)
                .setBold()
                .setFontColor(WebColors.getRGBColor("#b4894d"))
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(style));
    }

    private void setAnalysisSection(Document document, UUID analysisId) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Analysis id: " + analysisId.toString()));
    }

    private void setComponentsSection(Document document, List<ComponentReadDto> components) {
        com.itextpdf.layout.element.List componentsList = new com.itextpdf.layout.element.List();

        for(ComponentReadDto component : components) {
            StringBuilder componentRow = new StringBuilder("[" + component.getType() + "] ");
            componentRow.append("[").append(component.getCode()).append("] ");
            componentRow.append(component.getName());

            if(component.getFather() != null)
                componentRow.append(" [").append(component.getFather().getCode()).append("] ");

            if(!component.getResponsibilities().isEmpty()) {
                componentRow.append("\n[Responsibilities]:");

                for(ResponsibilityReadDto responsibility : component.getResponsibilities()){
                    componentRow.append("\n").append("\u200B    ")
                            .append("-[").append(responsibility.getCode()).append("] ")
                            .append(responsibility.getResponsibility());
                }
            }

            if(!component.getVariables().isEmpty()) {
                componentRow.append("\n[Variables]:");

                for(VariableReadDto variable : component.getVariables()) {
                    componentRow.append("\n").append("\u200B    ")
                            .append("-[").append(variable.getCode()).append("] ").append(variable.getName());

                    if(!variable.getStates().isEmpty()) {
                        for(StateReadDto state : variable.getStates())
                            componentRow.append("\n").append("\u200B        ")
                                    .append("-[").append(state.getCode()).append("] ").append(state.getName());
                    }
                }
            }

            componentsList.add(componentRow.toString());
        }

        document.add(new Paragraph("Components")
                .setBold()
                .setUnderline());
        document.add(componentsList);
    }

    private void setConnectionsSection(Document document, List<ConnectionReadDto> connections) {
        com.itextpdf.layout.element.List connectionsList = new com.itextpdf.layout.element.List();

        for(ConnectionReadDto connection : connections) {
            StringBuilder connectionRow = new StringBuilder("[" + connection.getCode() + "] ");
            connectionRow.append(connection.getSource().getName())
                    .append(" --> ")
                    .append(connection.getTarget().getName());

            for(ConnectionActionReadDto connectionAction : connection.getConnectionActions()){
                connectionRow.append("\n").append("\u200B    ")
                        .append("-[").append(connectionAction.getConnectionActionType().getFormattedName()).append("] ")
                        .append("[").append(connectionAction.getCode()).append("] ")
                        .append(connectionAction.getName());
            }

            connectionsList.add(connectionRow.toString());
        }


        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Connections")
                .setBold()
                .setUnderline());
        document.add(connectionsList);
    }

    private void setImagesSection(Document document, List<ImageReadDto> images) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Images")
                .setBold()
                .setUnderline());

        for(ImageReadDto image : images) {
            byte[] imageBytes = Base64.getDecoder()
                    .decode(image.getBase64());
            document.add(new Image(ImageDataFactory.create(imageBytes)));
        }
    }
}
