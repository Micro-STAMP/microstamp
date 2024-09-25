package microstamp.authorization.util.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import microstamp.authorization.dto.step2.*;

import java.util.Base64;
import java.util.List;

public class Step2PdfHelper {

    public static void setComponentsSection(Document document, List<ComponentReadDto> components) {
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

    public static void setConnectionsSection(Document document, List<ConnectionReadDto> connections) {
        com.itextpdf.layout.element.List connectionsList = new com.itextpdf.layout.element.List();

        for(ConnectionReadDto connection : connections) {
            StringBuilder connectionRow = new StringBuilder("[" + connection.getCode() + "] ");
            connectionRow.append(connection.getSource().getName())
                    .append(" --> ")
                    .append(connection.getTarget().getName());

            for(InteractionReadDto interaction : connection.getInteractions()){
                connectionRow.append("\n").append("\u200B    ")
                        .append("-[").append(interaction.getInteractionType().getFormattedName()).append("] ")
                        .append("[").append(interaction.getCode()).append("] ")
                        .append(interaction.getName());
            }

            connectionsList.add(connectionRow.toString());
        }


        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Connections")
                .setBold()
                .setUnderline());
        document.add(connectionsList);
    }

    public static void setImagesSection(Document document, List<ImageReadDto> images) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Images")
                .setBold()
                .setUnderline());

        if (images.isEmpty()) {
            document.add(new Paragraph("No images found"));
            return;
        }

        for(ImageReadDto image : images) {
            byte[] imageBytes = Base64.getDecoder()
                    .decode(image.getBase64());
            document.add(new Image(ImageDataFactory.create(imageBytes)));
        }
    }
}
