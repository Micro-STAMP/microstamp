package microstamp.cast.step4.controller;

import lombok.RequiredArgsConstructor;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorInsertDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorReadDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorUpdateDto;
import microstamp.cast.step4.service.SystemicFactorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/systemic-factors")
@RequiredArgsConstructor
public class SystemicFactorController {

    private final SystemicFactorService service;

    @PostMapping
    public ResponseEntity<SystemicFactorReadDto> create(@RequestBody SystemicFactorInsertDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<List<SystemicFactorReadDto>> findByAnalysisId(@PathVariable UUID analysisId) {
        return ResponseEntity.ok(service.findByAnalysisId(analysisId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SystemicFactorReadDto> update(@PathVariable UUID id, @RequestBody SystemicFactorUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
