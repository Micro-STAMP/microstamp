package step3.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import step3.dto.variable.*;
import step3.entity.Variable;
import step3.service.VariableService;
import java.util.List;

@RestController
@RequestMapping("/variable")
public class VariableController {
    private final VariableService variableService;

    // Constructors -----------------------------------

    @Autowired
    public VariableController(VariableService variableService) {
        this.variableService = variableService;
    }

    // Create -----------------------------------------

    @PostMapping @Transactional
    public ResponseEntity<VariableReadDto> createVariable(@RequestBody @Valid VariableCreateDto variableCreateDto, UriComponentsBuilder uriBuilder) {
        VariableReadDto variable = variableService.createVariable(variableCreateDto);
        URI uri = uriBuilder.path("/variable/{id}").buildAndExpand(variable.id()).toUri();
        return ResponseEntity.created(uri).body(variable);
    }

    // Read -------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<VariableReadDto> readVariable(@PathVariable Long id) {
        return ResponseEntity.ok(variableService.readVariable(id));
    }
    @GetMapping
    public ResponseEntity<List<VariableReadListDto>> readAllVariables() {
        return ResponseEntity.ok(variableService.readAllVariables());
    }

    @GetMapping("/controller/{controllerId}")
    public ResponseEntity<List<VariableReadListDto>> readVariablesByControllerId(@PathVariable Long controllerId) {
        return ResponseEntity.ok(variableService.readVariablesByControllerId(controllerId));
    }

    // Update -----------------------------------------

    @PutMapping("/{id}") @Transactional
    public ResponseEntity<VariableReadDto> updateVariable(@PathVariable Long id, @RequestBody VariableUpdateDto variable) {
        VariableReadDto updatedVariable = variableService.updateVariable(id, variable);
        return ResponseEntity.ok(updatedVariable);
    }

    // Delete -----------------------------------------

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteVariable(@PathVariable Long id) {
        variableService.deleteVariable(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------
}
