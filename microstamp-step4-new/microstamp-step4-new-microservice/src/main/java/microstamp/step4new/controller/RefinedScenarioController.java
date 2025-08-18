package microstamp.step4new.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseGroupReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioInsertDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioUpdateDto;
import microstamp.step4new.service.RefinedScenarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "RefinedScenarios")
@RequestMapping("/refined-scenarios")
public class RefinedScenarioController {

	private final RefinedScenarioService service;

	@GetMapping("/unsafe_control_action_id/{id}")
	public ResponseEntity<List<RefinedScenarioReadDto>> findByUnsafeControlActionId(@PathVariable("id") UUID id) {
		return new ResponseEntity<>(service.findByUnsafeControlActionId(id), HttpStatus.OK);
	}

	@GetMapping("/unsafe_control_action_id/{id}/common-causes")
	public ResponseEntity<List<RefinedScenarioCommonCauseGroupReadDto>> groupByCommonCause(@PathVariable("id") UUID id) {
		return new ResponseEntity<>(service.groupByCommonCauseForUnsafeControlAction(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<RefinedScenarioReadDto> insert(@Valid @RequestBody RefinedScenarioInsertDto dto) {
		return new ResponseEntity<>(service.insert(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody RefinedScenarioUpdateDto dto) {
		service.update(id, dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		service.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}