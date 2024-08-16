package step3.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import step3.dto.value.*;
import step3.entity.Value;
import step3.service.ValueService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/value")
public class ValueController {
    private final ValueService valueService;

    // Constructors -----------------------------------

    @Autowired
    public ValueController(ValueService valueService) {
        this.valueService = valueService;
    }

    // Create -----------------------------------------

    @PostMapping @Transactional
    public ResponseEntity<ValueReadDto> createValue(@RequestBody @Valid ValueCreateDto valueCreateDto, UriComponentsBuilder uriBuilder) {
        ValueReadDto value = valueService.createValue(valueCreateDto);
        URI uri = uriBuilder.path("/value/{id}").buildAndExpand(value.id()).toUri();
        return ResponseEntity.created(uri).body(value);
    }

    // Read -------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<ValueReadDto> readValue(@PathVariable Long id) {
        return ResponseEntity.ok(valueService.readValue(id));
    }
    @GetMapping
    public ResponseEntity<List<ValueReadDto>> readAllValue() {
        return ResponseEntity.ok(valueService.readAllValues());
    }

    // Update -----------------------------------------

    // Creio que não vai precisar ter um put em valor,
    // já que se alguem quiser trocar ele remove o antigo e cria um novo
    @PutMapping @Transactional
    public ResponseEntity<Value> updateValue(@RequestBody Value value) {
        valueService.updateValue(value);
        return ResponseEntity.ok(value);
    }

    // Delete -----------------------------------------

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteValue(@PathVariable Long id) {
        valueService.deleteValue(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------
}
