package step3.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import step3.dto.control_action.ControlActionCreateDto;
import step3.dto.control_action.ControlActionReadDto;
import step3.dto.control_action.ControlActionUpdateDto;
import step3.dto.unsafe_control_action.UnsafeControlActionContextDto;
import step3.service.ControlActionService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/control-action")
public class ControlActionController {
    private final ControlActionService controlActionService;

    // Constructors -----------------------------------

    @Autowired
    public ControlActionController(ControlActionService controlActionService) {
        this.controlActionService = controlActionService;
    }

    // Create -----------------------------------------

    @PostMapping @Transactional
    public ResponseEntity<ControlActionReadDto> createControlAction(@RequestBody @Valid ControlActionCreateDto controlActionCreateDto, UriComponentsBuilder uriBuilder) {
        ControlActionReadDto controlAction = controlActionService.createControlAction(controlActionCreateDto);
        URI uri = uriBuilder.path("/control-action/{id}").buildAndExpand(controlAction.id()).toUri();
        return ResponseEntity.created(uri).body(controlAction);
    }

    // Read -------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<ControlActionReadDto> readControlAction(@PathVariable Long id) {
        return ResponseEntity.ok(controlActionService.readControlAction(id));
    }
    @GetMapping
    public ResponseEntity<List<ControlActionReadDto>> readAllControlActions() {
       return ResponseEntity.ok(controlActionService.readAllControlActions());
    }

    @GetMapping("/controller/{controllerId}")
    public ResponseEntity<List<ControlActionReadDto>> readControlActionsByControllerId(@PathVariable Long controllerId) {
        return ResponseEntity.ok(controlActionService.readControlActionsByControllerId(controllerId));
    }

    @GetMapping("{id}/uca-context")
    public ResponseEntity<List<UnsafeControlActionContextDto>> readControlActionContext(@PathVariable Long id) {
        return ResponseEntity.ok(controlActionService.readUnsafeControlActionContext(id));
    }

    // Update -----------------------------------------

    @PutMapping("/{id}") @Transactional
    public ResponseEntity<ControlActionReadDto> updateControlAction(@PathVariable Long id, @RequestBody ControlActionUpdateDto controlAction) {
        ControlActionReadDto updatedControlAction = controlActionService.updateControlAction(id, controlAction);
        return ResponseEntity.ok(updatedControlAction);
    }

    // Delete -----------------------------------------

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteControlAction(@PathVariable Long id) {
        controlActionService.deleteControlAction(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------
}
