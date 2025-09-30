package microstamp.step4new.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto;
import microstamp.step4new.service.RefinedScenarioCommonCauseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "RefinedScenariosCommonCauses")
@RequestMapping("/refined-scenarios/common-causes")
public class RefinedScenarioCommonCauseController {

	private final RefinedScenarioCommonCauseService service;

	@GetMapping
	public ResponseEntity<List<RefinedScenarioCommonCauseReadDto>> findAll() {
		return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("/code/{code}")
	public ResponseEntity<RefinedScenarioCommonCauseReadDto> findByCode(@PathVariable("code") String code) {
		return new ResponseEntity<>(service.findByCode(code), HttpStatus.OK);
	}
}