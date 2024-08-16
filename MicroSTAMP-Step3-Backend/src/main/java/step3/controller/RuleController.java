package step3.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import step3.dto.rule.*;
import step3.service.RuleService;
import java.util.List;

@RestController
@RequestMapping("/rule")
public class RuleController {
    private final RuleService ruleService;

    // Constructors -----------------------------------

    @Autowired
    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    // Create -----------------------------------------

    @PostMapping @Transactional
    public ResponseEntity<RuleReadDto> createRule(@RequestBody @Valid RuleCreateDto ruleCreateDto, UriComponentsBuilder uriBuilder) {
        RuleReadDto rule = ruleService.createRule(ruleCreateDto);
        URI uri = uriBuilder.path("/rule/{id}").buildAndExpand(rule.id()).toUri();
        return ResponseEntity.created(uri).body(rule);
    }

    // Read -------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<RuleReadDto> readRule(@PathVariable Long id) {
        return ResponseEntity.ok(ruleService.readRule(id));
    }
    @GetMapping
    public ResponseEntity<List<RuleReadListDto>> readAllRules() {
        return ResponseEntity.ok(ruleService.readAllRules());
    }

    @GetMapping("/control-action/{controlActionId}")
    public ResponseEntity<List<RuleReadListDto>> readRulesByControlActionId(@PathVariable Long controlActionId) {
        return ResponseEntity.ok(ruleService.readRulesByControlActionId(controlActionId));
    }

    // Update -----------------------------------------

    // Delete -----------------------------------------

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------
}
