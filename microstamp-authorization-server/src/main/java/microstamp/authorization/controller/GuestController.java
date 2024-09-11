package microstamp.authorization.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.authorization.dto.ExportReadDto;
import microstamp.authorization.service.GuestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "Guest")
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    @GetMapping(path = {"analysis"})
    public ResponseEntity<List<ExportReadDto>> findGuestsAnalyses() {
        log.info("Request received to find guest analyses");
        return new ResponseEntity<>(guestService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"analysis/{id}"})
    public ResponseEntity<ExportReadDto> findGuestsAnalysisById(@PathVariable("id") UUID id) {
        log.info("Request received to find a guest analysis by its id {}", id);
        return new ResponseEntity<>(guestService.findById(id), HttpStatus.OK);
    }
}
