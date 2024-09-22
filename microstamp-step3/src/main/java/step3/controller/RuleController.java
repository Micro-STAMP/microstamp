package step3.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import step3.dto.rule.RuleCreateDto;
import step3.dto.rule.RuleReadDto;
import step3.dto.rule.RuleReadListDto;
import step3.service.RuleService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
@Tag(name = "Rule")
public class RuleController {
    private final RuleService ruleService;

    @Autowired
    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping @Transactional
    public ResponseEntity<RuleReadDto> createRule(@RequestBody @Valid RuleCreateDto ruleCreateDto, UriComponentsBuilder uriBuilder) {
        RuleReadDto rule = ruleService.createRule(ruleCreateDto);
        URI uri = uriBuilder.path("/rule/{id}").buildAndExpand(rule.id()).toUri();
        return ResponseEntity.created(uri).body(rule);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RuleReadDto> readRule(@PathVariable UUID id) {
        return ResponseEntity.ok(ruleService.readRule(id));
    }
    @GetMapping
    public ResponseEntity<List<RuleReadListDto>> readAllRules() {
        return ResponseEntity.ok(ruleService.readAllRules());
    }

    @GetMapping("/control-action/{controlActionId}")
    public ResponseEntity<List<RuleReadListDto>> readRulesByControlActionId(@PathVariable UUID controlActionId) {
        return ResponseEntity.ok(ruleService.readRulesByControlActionId(controlActionId));
    }

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}
