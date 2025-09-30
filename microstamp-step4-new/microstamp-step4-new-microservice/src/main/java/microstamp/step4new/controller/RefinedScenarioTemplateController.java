package microstamp.step4new.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateReadDto;
import microstamp.step4new.service.RefinedScenarioTemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "RefinedScenariosTemplates")
@RequestMapping("/refined-scenarios/templates")
public class RefinedScenarioTemplateController {

	private final RefinedScenarioTemplateService service;

	@GetMapping("/common-cause/{code}")
	public ResponseEntity<List<RefinedScenarioTemplateReadDto>> findByCommonCauseCode(@PathVariable("code") String code) {
		return new ResponseEntity<>(service.findByCommonCauseCode(code), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<RefinedScenarioTemplateReadDto>> findAll() {
		return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("/unsafe-control-action/{id}/apply")
	public ResponseEntity<List<RefinedScenarioTemplateReadDto>> applyTemplatesToUca(@PathVariable("id") java.util.UUID id) {
		return new ResponseEntity<>(service.applyTemplates(id), HttpStatus.OK);
	}

	@GetMapping("/unsafe-control-action/{id}/common-cause/{code}/apply")
	public ResponseEntity<List<RefinedScenarioTemplateReadDto>> applyTemplatesToUcaByCommonCause(@PathVariable("id") java.util.UUID id, @PathVariable("code") String code) {
		return new ResponseEntity<>(service.applyTemplatesByCommonCause(id, code), HttpStatus.OK);
	}

	@GetMapping("/unsafe-control-action/{id}/apply/common-cause")
	public ResponseEntity<java.util.List<microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto>> applyTemplatesGroupedByCommonCause(@PathVariable("id") java.util.UUID id) {
		return new ResponseEntity<>(service.applyTemplatesGroupedByCommonCause(id), HttpStatus.OK);
	}
}