package step3.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import step3.dto.context_table.ContextTableCreateDto;
import step3.dto.context_table.ContextTableReadDto;
import step3.dto.context_table.ContextTableReadWithPageDto;
import step3.service.ContextTableService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/context-table")
public class ContextTableController {
    private final ContextTableService contextTableService;

    @Autowired
    public ContextTableController(ContextTableService contextTableService) {
        this.contextTableService = contextTableService;
    }

    @PostMapping @Transactional
    public ResponseEntity<ContextTableReadDto> createContextTable(@RequestBody @Valid ContextTableCreateDto contextTableCreateDto, UriComponentsBuilder uriBuilder) {
        ContextTableReadDto contextTable = contextTableService.createContextTable(contextTableCreateDto);
        URI uri = uriBuilder.path("/context-table/{id}").buildAndExpand(contextTable.id()).toUri();
        return ResponseEntity.created(uri).body(contextTable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContextTableReadWithPageDto> readContextTableById(@PathVariable UUID id,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(contextTableService.readContextTableById(id, page, size));
    }

    @GetMapping
    public ResponseEntity<List<ContextTableReadDto>> readAllContextTables() {
        return ResponseEntity.ok(contextTableService.readAllContextTables());
    }

    @GetMapping("/control-action/{controlActionId}")
    public ResponseEntity<ContextTableReadWithPageDto> readContextTableByControlActionId(@PathVariable UUID controlActionId,
                                                                                         @RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(contextTableService.readContextTableByControlActionId(controlActionId, page, size));
    }

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteContextTable(@PathVariable UUID id) {
        contextTableService.deleteContextTable(id);
        return ResponseEntity.noContent().build();
    }
}
