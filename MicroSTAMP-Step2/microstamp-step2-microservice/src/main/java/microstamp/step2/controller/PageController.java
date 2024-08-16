package microstamp.step2.controller;

import microstamp.step2.dto.component.ComponentReadDto;
import microstamp.step2.entity.Component;
import microstamp.step2.enumeration.ConnectionActionType;
import microstamp.step2.enumeration.Style;
import microstamp.step2.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class PageController {

    @Autowired
    private ComponentServiceImpl componentService;

    @Autowired
    private ConnectionServiceImpl connectionService;

    @Autowired
    private ImageServiceImpl imageService;

    @Autowired
    private VariableServiceImpl variableService;

    @GetMapping("/{analysisId:\\d+}")
    public String indexPage(@PathVariable UUID analysisId, Model model) {

        List<ComponentReadDto> components = componentService.findByAnalysisId(analysisId);
        model.addAttribute("components", components);
        model.addAttribute("connections", connectionService.findByAnalysisId(analysisId));
        model.addAttribute("control_structure_id", analysisId);
        model.addAttribute("connectionActionType", ConnectionActionType.values());
        model.addAttribute("variables", variableService.findByAnalysisId(analysisId));

        model.addAttribute("images", imageService.findByAnalysisId(analysisId));

        model.addAttribute("style", Style.values());

        List<ComponentReadDto> componentsWithoutEnvironment = componentService.findByAnalysisId(analysisId);
        if (!componentsWithoutEnvironment.isEmpty())
            componentsWithoutEnvironment.removeFirst();
        model.addAttribute("componentsWithoutEnvironment", componentsWithoutEnvironment);

        List<Component> controllersControlledProcess = new ArrayList<>();
        /*for (ComponentReadDto c : components) {
            if (c.getType().equals(ComponentType.CONTROLLER.name()) || c.getType().equals(ComponentType.CONTROLLED_PROCESS.name())) {
                controllersControlledProcess.add(c);
            }
        }*/
        model.addAttribute("controllersControlledProcess", controllersControlledProcess);

        return "index";
    }

    @GetMapping("/home")
    public String controlStructures(Model model) {
        //model.addAttribute("controlStructures", controlStructureService.findAll());
        return "control_structures";
    }

    @GetMapping("/")
    public String redirectHome(Model model) {
        return controlStructures(model);
    }

    @GetMapping("/guests")
    public String guests(Model model) {
        //model.addAttribute("controlStructures", controlStructureService.findAnalysisForGuests());
        return "guests";
    }

    @GetMapping("/guests/{analysisId}")
    public String indexPageGuest(@PathVariable UUID analysisId, Model model) {

        List<ComponentReadDto> components = componentService.findByAnalysisId(analysisId);
        model.addAttribute("components", components);
        model.addAttribute("connections", connectionService.findByAnalysisId(analysisId));
        model.addAttribute("control_structure_id", analysisId);
        model.addAttribute("variables", variableService.findByAnalysisId(analysisId));
        model.addAttribute("images", imageService.findByAnalysisId(analysisId));

        return "guestsIndex";
    }
}
