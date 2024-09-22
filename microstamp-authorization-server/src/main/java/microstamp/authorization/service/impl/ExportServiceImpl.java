package microstamp.authorization.service.impl;

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
import microstamp.authorization.client.MicroStampStep1Client;
import microstamp.authorization.client.MicroStampStep2Client;
import microstamp.authorization.client.MicroStampStep3Client;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.ExportReadDto;
import microstamp.authorization.dto.step1.*;
import microstamp.authorization.dto.step2.Step2ExportReadDto;
import microstamp.authorization.dto.step3.Step3ExportReadDto;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.ExportService;
import microstamp.authorization.util.pdf.Step1PdfHelper;
import microstamp.authorization.util.pdf.Step2PdfHelper;
import microstamp.authorization.util.pdf.Step3PdfHelper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final AnalysisService analysisService;

    private final MicroStampStep1Client step1Client;

    private final MicroStampStep2Client step2Client;

    private final MicroStampStep3Client step3Client;

    public ExportReadDto exportToJson(UUID analysisId, String guestJwt) {
        log.info("Exporting (JSON) content of an analysis by its UUID: {}", analysisId);

        return guestJwt.isEmpty()
                ? getExportDto(analysisId)
                : getExportDto(analysisId, guestJwt);
    }

    public byte[] exportToPdf(UUID analysisId, String guestJwt) throws IOException {
        log.info("Exporting (PDF) content of an analysis by its UUID: {}", analysisId);

        ExportReadDto exportReadDto = guestJwt.isEmpty()
                ? getExportDto(analysisId)
                : getExportDto(analysisId, guestJwt);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        buildPDF(document, exportReadDto);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private ExportReadDto getExportDto(UUID analysisId) {
        return ExportReadDto.builder()
                .analysis(analysisService.findById(analysisId))
                .step1(step1Client.exportStep1ByAnalysisId(analysisId))
                .step2(step2Client.exportStep2ByAnalysisId(analysisId))
                .step3(step3Client.exportStep3ByAnalysisId(analysisId))
                .build();
    }

    private ExportReadDto getExportDto(UUID analysisId, String guestJwt) {
        return ExportReadDto.builder()
                .analysis(analysisService.findById(analysisId))
                .step1(step1Client.exportStep1ByAnalysisId(guestJwt, analysisId))
                .step2(step2Client.exportStep2ByAnalysisId(guestJwt, analysisId))
                .step3(step3Client.exportStep3ByAnalysisId(guestJwt, analysisId))
                .build();
    }

    private void buildPDF(Document document, ExportReadDto exportReadDto) throws IOException {
        setTitle(document, exportReadDto.getAnalysis());
        setAnalysisSection(document, exportReadDto.getAnalysis());
        setStep1Section(document, exportReadDto.getStep1());
        setStep2Section(document, exportReadDto.getStep2());
        setStep3Section(document, exportReadDto.getStep3());
    }

    private void setTitle(Document document, AnalysisReadDto analysis) throws IOException {
        Style style = new Style();
        style.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC))
            .setFontSize(20)
            .setBold()
            .setFontColor(WebColors.getRGBColor("#b4894d"))
            .setTextAlignment(TextAlignment.CENTER);

        document.add(new Paragraph("MICROSTAMP ANALYSIS - " + analysis.getName())
                .addStyle(style));
    }

    private void setAnalysisSection(Document document, AnalysisReadDto analysis) {
        com.itextpdf.layout.element.List analysisDetails = new com.itextpdf.layout.element.List();

        analysisDetails.add("ID: " + analysis.getId());
        analysisDetails.add("Name: " + analysis.getName());
        analysisDetails.add("Description: " + analysis.getDescription());

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Analysis")
                .setBold()
                .setUnderline());
        document.add(analysisDetails);
        document.add(new Paragraph("\n"));

        if (analysis.getImage() != null) {
            byte[] imageBytes = Base64.getDecoder()
                    .decode(analysis.getImage().getBase64());
            document.add(new Image(ImageDataFactory.create(imageBytes)).setAutoScale(true));
        }
    }

    private void setStep1Section(Document document, Step1ExportReadDto step1Dto) throws IOException {
        setSectionTitle(document, "1 - Define Purpose of the Analysis");

        Step1PdfHelper.setSystemGoalSection(document, step1Dto.getSystemGoals());
        Step1PdfHelper.setAssumptionSection(document, step1Dto.getAssumptions());
        Step1PdfHelper.setLossesSection(document, step1Dto.getLosses());
        Step1PdfHelper.setHazardsSection(document, step1Dto.getHazards());
        Step1PdfHelper.setSystemSafetyConstraintSection(document, step1Dto.getSystemSafetyConstraints());
    }

    private void setStep2Section(Document document, Step2ExportReadDto step2Dto) throws IOException {
        setSectionTitle(document, "2 - Model the Control Structure");

        Step2PdfHelper.setComponentsSection(document, step2Dto.getComponents());
        Step2PdfHelper.setConnectionsSection(document, step2Dto.getConnections());
        Step2PdfHelper.setImagesSection(document, step2Dto.getImages());
    }

    private void setStep3Section(Document document, Step3ExportReadDto step3Dto) throws IOException {
        setSectionTitle(document, "3 - Identify Unsafe Control Actions");

        Step3PdfHelper.setUcaAndConstraintSection(document, step3Dto.getUnsafeControlActions());
        Step3PdfHelper.setRuleSection(document, step3Dto.getRules());
    }

    private void setSectionTitle(Document document, String title) throws IOException {
        document.add(new Paragraph("\n"));

        document.add(new Paragraph(title).addStyle(new Style()
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC))
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)));

        document.add(new Paragraph("\n"));
    }
}