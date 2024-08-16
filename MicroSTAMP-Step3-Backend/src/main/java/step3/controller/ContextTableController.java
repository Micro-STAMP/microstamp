package step3.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import step3.dto.context_table.*;
import step3.service.ContextTableService;
import java.util.List;

@RestController
@RequestMapping("/context-table")
public class ContextTableController {
    private final ContextTableService contextTableService;

    // Constructors -----------------------------------

    @Autowired
    public ContextTableController(ContextTableService contextTableService) {
        this.contextTableService = contextTableService;
    }

    // Create -----------------------------------------

    @PostMapping @Transactional
    public ResponseEntity<ContextTableReadDto> createContextTable(@RequestBody @Valid ContextTableCreateDto contextTableCreateDto, UriComponentsBuilder uriBuilder) {
        ContextTableReadDto contextTable = contextTableService.createContextTable(contextTableCreateDto);
        URI uri = uriBuilder.path("/context-table/{id}").buildAndExpand(contextTable.id()).toUri();
        return ResponseEntity.created(uri).body(contextTable);
    }

    // Read -------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<ContextTableReadDto> readContextTableById(@PathVariable Long id) {
        return ResponseEntity.ok(contextTableService.readContextTableById(id));
    }
    @GetMapping
    public ResponseEntity<List<ContextTableReadDto>> readAllContextTables() {
        return ResponseEntity.ok(contextTableService.readAllContextTables());
    }

    @GetMapping("/controller/{controllerId}")
    public ResponseEntity<ContextTableReadWithPageDto> readContextTableByControllerId(@PathVariable Long controllerId,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(contextTableService.readContextTableByControllerId(controllerId, page, size));
    }

    // Update -----------------------------------------


    // Delete -----------------------------------------

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteContextTable(@PathVariable Long id) {
        contextTableService.deleteContextTable(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------
}
