package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import microstamp.step2.dto.controlstructure.ControlStructureReadDto;
import microstamp.step2.service.ControlStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/control-structures")
@Tag(name = "ControlStructure")
public class ControlStructureController {

    @Autowired
    private ControlStructureService controlStructureService;

    @GetMapping("/analysis/{id}")
    public ResponseEntity<ControlStructureReadDto> findByAnalysisId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(controlStructureService.findByAnalysisId(id), HttpStatus.OK);
    }
}
