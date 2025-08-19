package microstamp.step4new.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.mitigation.MitigationInsertDto;
import microstamp.step4new.dto.mitigation.MitigationReadDto;
import microstamp.step4new.dto.mitigation.MitigationUpdateDto;
import microstamp.step4new.service.MitigationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "Mitigation")
@RequestMapping("/mitigations")
public class MitigationController {

	private final MitigationService service;

	@GetMapping("/{id}")
	public ResponseEntity<MitigationReadDto> findById(@PathVariable("id") UUID id) {
		return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
	}

	@GetMapping("/refined-scenario/{id}")
	public ResponseEntity<MitigationReadDto> getOrCreateByRefinedScenarioId(@PathVariable("id") UUID id) {
		return new ResponseEntity<>(service.getOrCreateByRefinedScenarioId(id), HttpStatus.OK);
	}

	@GetMapping("/unsafe-control-action/{id}")
	public ResponseEntity<List<MitigationReadDto>> findByUnsafeControlActionId(@PathVariable("id") UUID id) {
		return new ResponseEntity<>(service.findByUnsafeControlActionId(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<MitigationReadDto> create(@Valid @RequestBody MitigationInsertDto dto) {
		return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody MitigationUpdateDto dto) {
		service.update(id, dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		service.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}


