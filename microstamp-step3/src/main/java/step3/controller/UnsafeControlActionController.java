package step3.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import step3.dto.unsafe_control_action.UnsafeControlActionCreateDto;
import step3.dto.unsafe_control_action.UnsafeControlActionReadDto;
import step3.service.UnsafeControlActionService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/unsafe-control-action")
@Tag(name = "UnsafeControlAction")
public class UnsafeControlActionController {
    private final UnsafeControlActionService unsafeControlActionService;

    @Autowired
    public UnsafeControlActionController(UnsafeControlActionService unsafeControlActionService) {
        this.unsafeControlActionService = unsafeControlActionService;
    }

    @PostMapping @Transactional
    public ResponseEntity<UnsafeControlActionReadDto> createUnsafeControlAction(@RequestBody @Valid UnsafeControlActionCreateDto unsafeControlActionCreateDto, UriComponentsBuilder uriBuilder) {
        UnsafeControlActionReadDto uca = unsafeControlActionService.createUnsafeControlAction(unsafeControlActionCreateDto);
        URI uri = uriBuilder.path("/unsafe-control-action/{id}").buildAndExpand(uca.id()).toUri();
        return ResponseEntity.created(uri).body(uca);
    }
    @PostMapping("/rule/{rule_id}") @Transactional
    public ResponseEntity<List<UnsafeControlActionReadDto>> createUCAsByRule(@PathVariable UUID rule_id) {
        List<UnsafeControlActionReadDto> createdUCAs = unsafeControlActionService.createUCAsByRule(rule_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUCAs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnsafeControlActionReadDto> readUnsafeControlAction(@PathVariable UUID id) {
        return  ResponseEntity.ok(unsafeControlActionService.readUnsafeControlAction(id));
    }

    @GetMapping
    public ResponseEntity<List<UnsafeControlActionReadDto>> readAllUnsafeControlAction() {
        return ResponseEntity.ok(unsafeControlActionService.readAllUnsafeControlActions());
    }

    @GetMapping("/control-action/{controlActionId}")
    public ResponseEntity<List<UnsafeControlActionReadDto>> readAllUCAByControlActionId(@PathVariable UUID controlActionId) {
        return ResponseEntity.ok(unsafeControlActionService.readAllUCAByControlActionId(controlActionId));
    }

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteUnsafeControlAction(@PathVariable UUID id) {
        unsafeControlActionService.deleteUnsafeControlAction(id);
        return ResponseEntity.noContent().build();
    }
}
