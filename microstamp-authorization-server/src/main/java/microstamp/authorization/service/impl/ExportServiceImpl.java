package microstamp.authorization.service.impl;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.authorization.client.MicroStampStep1Client;
import microstamp.authorization.client.MicroStampStep2Client;
import microstamp.authorization.client.MicroStampStep3Client;
import microstamp.authorization.client.MicroStampStep4Client;
import microstamp.authorization.client.MicroStampStep4NewClient;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.ExportReadDto;
import microstamp.authorization.dto.step1.*;
import microstamp.authorization.dto.step2.Step2ExportReadDto;
import microstamp.authorization.dto.step3.Step3ExportReadDto;
import microstamp.authorization.dto.step4.Step4ExportReadDto;
import microstamp.authorization.dto.step4new.Step4NewExportReadDto;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.ExportService;
import microstamp.authorization.util.pdf.Step1PdfHelper;
import microstamp.authorization.util.pdf.Step2PdfHelper;
import microstamp.authorization.util.pdf.Step3PdfHelper;
import microstamp.authorization.util.pdf.Step4PdfHelper;
import microstamp.authorization.util.pdf.Step4NewPdfHelper;
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

    private final MicroStampStep4Client step4Client;

    private final MicroStampStep4NewClient step4NewClient;

    private static final Color MICROSTAMP_YELLOW = new DeviceRgb(212, 160, 86);     // #d4a056
    private static final Color MICROSTAMP_DARK_YELLOW = new DeviceRgb(180, 137, 77); // #b4894d
    private static final Color MICROSTAMP_GRAY = new DeviceRgb(74, 76, 92);         // #4a4c5c

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
        pdfDocument.setDefaultPageSize(PageSize.A4);

        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new PageNumberHandler());
        
        Document document = new Document(pdfDocument);
        document.setMargins(60, 60, 60, 60);

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
                .step4(step4Client.exportStep4ByAnalysisId(analysisId))
                .step4new(step4NewClient.exportStep4NewByAnalysisId(analysisId))
                .build();
    }

    private ExportReadDto getExportDto(UUID analysisId, String guestJwt) {
        return ExportReadDto.builder()
                .analysis(analysisService.findById(analysisId))
                .step1(step1Client.exportStep1ByAnalysisId(guestJwt, analysisId))
                .step2(step2Client.exportStep2ByAnalysisId(guestJwt, analysisId))
                .step3(step3Client.exportStep3ByAnalysisId(guestJwt, analysisId))
                .step4(step4Client.exportStep4ByAnalysisId(guestJwt, analysisId))
                .step4new(step4NewClient.exportStep4NewByAnalysisId(guestJwt, analysisId))
                .build();
    }

    private void buildPDF(Document document, ExportReadDto exportReadDto) throws IOException {
        // Cover Page
        createCoverPage(document, exportReadDto.getAnalysis());
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        
        // Table of Contents
        createTableOfContents(document);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        
        // Analysis Information
        setAnalysisSection(document, exportReadDto.getAnalysis());
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        
        // Step 1
        setStep1Section(document, exportReadDto.getStep1());
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        
        // Step 2
        setStep2Section(document, exportReadDto.getStep2());
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        
        // Step 3
        setStep3Section(document, exportReadDto.getStep3());
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        
        // Step 4
        setStep4Section(document, exportReadDto.getStep4());
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        
        // Step 4 New
        setStep4NewSection(document, exportReadDto.getStep4new());
    }


    private void setAnalysisSection(Document document, AnalysisReadDto analysis) throws IOException {
        PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont contentFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        Paragraph sectionTitle = new Paragraph("Analysis Information")
                .setFont(titleFont)
                .setFontSize(18)
                .setFontColor(MICROSTAMP_YELLOW)
                .setMarginBottom(20);
        sectionTitle.setDestination("Analysis");
        document.add(sectionTitle);

        document.add(new Paragraph("Name: " + analysis.getName())
                .setFont(contentFont)
                .setFontSize(12)
                .setMarginBottom(8));

        document.add(new Paragraph("Description: " + analysis.getDescription())
                .setFont(contentFont)
                .setFontSize(12)
                .setMarginBottom(20));

        if (analysis.getImage() != null) {
            byte[] imageBytes = Base64.getDecoder()
                    .decode(analysis.getImage().getBase64());
            Image image = new Image(ImageDataFactory.create(imageBytes))
                    .setAutoScale(true)
                    .setMaxWidth(400)
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            document.add(image);
        }
    }

    private void setStep1Section(Document document, Step1ExportReadDto step1Dto) throws IOException {
        setSectionTitle(document, "1 - Define Purpose of the Analysis", "Step1");

        Step1PdfHelper.setSystemGoalSection(document, step1Dto.getSystemGoals());
        Step1PdfHelper.setAssumptionSection(document, step1Dto.getAssumptions());
        Step1PdfHelper.setLossesSection(document, step1Dto.getLosses());
        Step1PdfHelper.setHazardsSection(document, step1Dto.getHazards());
        Step1PdfHelper.setSystemSafetyConstraintSection(document, step1Dto.getSystemSafetyConstraints());
    }

    private void setStep2Section(Document document, Step2ExportReadDto step2Dto) throws IOException {
        setSectionTitle(document, "2 - Model the Control Structure", "Step2");

        Step2PdfHelper.setComponentsSection(document, step2Dto.getComponents());
        Step2PdfHelper.setConnectionsSection(document, step2Dto.getConnections());
        Step2PdfHelper.setImagesSection(document, step2Dto.getImages());
    }

    private void setStep3Section(Document document, Step3ExportReadDto step3Dto) throws IOException {
        setSectionTitle(document, "3 - Identify Unsafe Control Actions", "Step3");

        Step3PdfHelper.setUcaAndConstraintSection(document, step3Dto.getUnsafeControlActions());
        Step3PdfHelper.setRuleSection(document, step3Dto.getRules());
    }

    private void setStep4Section(Document document, Step4ExportReadDto step4Dto) throws IOException {
        setSectionTitle(document, "4 - Identify Loss Scenarios (Handbook approach)", "Step4");

        //Step4PdfHelper.setFourTuplesSection(document, step4Dto.getFourTuples());
        Step4PdfHelper.setUnsafeControlActionsSection(document, step4Dto.getUnsafeControlActions());
    }

    private void setStep4NewSection(Document document, Step4NewExportReadDto step4NewDto) throws IOException {
        setSectionTitle(document, "4 - Identify Loss Scenarios (Formal approach)", "Step4new");

        Step4NewPdfHelper.setStep4NewContent(document, step4NewDto, step3Client);
    }

    private void setSectionTitle(Document document, String title, String destination) throws IOException {
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        
        document.add(new Paragraph("\n"));

        Paragraph titleParagraph = new Paragraph(title)
                .setFont(font)
                .setFontSize(18)
                .setFontColor(MICROSTAMP_YELLOW)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(16);
                
        titleParagraph.setDestination(destination);
        document.add(titleParagraph);

        document.add(new Paragraph("\n"));
    }

    private void createCoverPage(Document document, AnalysisReadDto analysis) throws IOException {
        PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont subtitleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        document.add(new Paragraph("\n\n\n\n\n\n"));

        document.add(new Paragraph("MicroSTAMP Analysis")
                .setFont(titleFont)
                .setFontSize(32)
                .setFontColor(MICROSTAMP_YELLOW)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));

        document.add(new Paragraph(analysis.getName())
                .setFont(titleFont)
                .setFontSize(24)
                .setFontColor(MICROSTAMP_DARK_YELLOW)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10));

        if (analysis.getDescription() != null && !analysis.getDescription().trim().isEmpty()) {
            document.add(new Paragraph(analysis.getDescription())
                    .setFont(subtitleFont)
                    .setFontSize(14)
                    .setFontColor(DeviceRgb.BLACK)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(40));
        }

        document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n"));

        document.add(new Paragraph("Powered by MicroSTAMP")
                .setFont(subtitleFont)
                .setFontSize(16)
                .setFontColor(MICROSTAMP_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10));

        document.add(new Paragraph("Microservices for System-Theoretic Process Analysis")
                .setFont(subtitleFont)
                .setFontSize(12)
                .setFontColor(MICROSTAMP_GRAY)
                .setTextAlignment(TextAlignment.CENTER));
    }

    private void createTableOfContents(Document document) throws IOException {
        PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont contentFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        document.add(new Paragraph("Table of Contents")
                .setFont(titleFont)
                .setFontSize(24)
                .setFontColor(MICROSTAMP_YELLOW)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(30));

        addTocEntry(document, contentFont, "Analysis Information", "Analysis");
        addTocEntry(document, contentFont, "1 - Define Purpose of the Analysis", "Step1");
        addTocEntry(document, contentFont, "2 - Model the Control Structure", "Step2");
        addTocEntry(document, contentFont, "3 - Identify Unsafe Control Actions", "Step3");
        addTocEntry(document, contentFont, "4 - Identify Loss Scenarios (Handbook approach)", "Step4");
        addTocEntry(document, contentFont, "4 - Identify Loss Scenarios (Formal approach)", "Step4new");
    }

    private void addTocEntry(Document document, PdfFont font, String title, String destination) {
        Paragraph tocEntry = new Paragraph()
                .setFont(font)
                .setFontSize(14)
                .setFontColor(MICROSTAMP_DARK_YELLOW)
                .setMarginBottom(8);

        tocEntry.add(new Link(title, PdfAction.createGoTo(destination)));
        
        document.add(tocEntry);
    }

    private static class PageNumberHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDocument = documentEvent.getDocument();
            PdfPage page = documentEvent.getPage();
            
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDocument);
            
            try {
                Canvas canvas = new Canvas(pdfCanvas, pageSize);
                PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                
                canvas.showTextAligned(
                    new Paragraph("Page " + pdfDocument.getPageNumber(page)).setFont(font),
                    pageSize.getWidth() / 2,
                    30,
                    TextAlignment.CENTER
                );
                
                canvas.close();
            } catch (IOException ignored) {
            }
        }
    }
}