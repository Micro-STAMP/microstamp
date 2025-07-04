package microstamp.authorization.util.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import microstamp.authorization.dto.step2.*;

import java.util.Base64;
import java.util.List;

public class Step2PdfHelper {

    public static void setComponentsSection(Document document, List<ComponentReadDto> components) {
        com.itextpdf.layout.element.List componentsList = new com.itextpdf.layout.element.List();

        for(ComponentReadDto component : components) {
            Paragraph componentParagraph = new Paragraph("[" + component.getType() + "] [" + component.getCode() + "] " + component.getName());
            componentParagraph.setDestination(component.getCode());

            if (component.getFather() != null) {
                componentParagraph.add(" [" + component.getFather().getCode() + "] ");
                componentParagraph.setDestination(component.getFather().getCode());
            }

            if(!component.getResponsibilities().isEmpty()) {
                componentParagraph.add("\n[Responsibilities]:");

                for(ResponsibilityReadDto responsibility : component.getResponsibilities()){
                    componentParagraph.add("\n\u200B    -[" + responsibility.getCode() + "] " + responsibility.getResponsibility());
                    componentParagraph.setDestination(responsibility.getCode());
                }
            }

            if (!component.getVariables().isEmpty()) {
                componentParagraph.add("\n[Variables]:");

                for(VariableReadDto variable : component.getVariables()) {
                    componentParagraph.add("\n\u200B    -[" + variable.getCode() + "] " + variable.getName());
                    componentParagraph.setDestination(variable.getCode());

                    if(!variable.getStates().isEmpty()) {
                        for(StateReadDto state : variable.getStates()) {
                            componentParagraph.add("\n\u200B        -[" + state.getCode() + "] " + state.getName());
                            componentParagraph.setDestination(state.getCode());
                        }
                    }
                }
            }

            ListItem componentItem = new ListItem();
            componentItem.add(componentParagraph);
            componentsList.add(componentItem);
        }

        document.add(new Paragraph("Components")
                .setBold()
                .setUnderline());
        document.add(componentsList);
    }

    public static void setConnectionsSection(Document document, List<ConnectionReadDto> connections) {
        com.itextpdf.layout.element.List connectionsList = new com.itextpdf.layout.element.List();

        for(ConnectionReadDto connection : connections) {
            Paragraph connectionParagraph = new Paragraph("[" + connection.getCode() + "] ");
            connectionParagraph.add(connection.getSource().getName() + " --> " + connection.getTarget().getName());
            connectionParagraph.setDestination(connection.getCode());

            for (InteractionReadDto interaction : connection.getInteractions()) {
                connectionParagraph.add("\n\u200B    -[" + interaction.getInteractionType().getFormattedName() + "] "
                        + "[" + interaction.getCode() + "] " + interaction.getName());
                connectionParagraph.setDestination(interaction.getCode());
            }

            ListItem connectionItem = new ListItem();
            connectionItem.add(connectionParagraph);
            connectionsList.add(connectionItem);
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
