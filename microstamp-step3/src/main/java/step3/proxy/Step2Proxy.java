package step3.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import step3.dto.mit.step2.ComponentReadDto;
import step3.dto.mit.step2.ControlActionReadDto;
import step3.dto.mit.step2.StateReadDto;
import step3.dto.mit.step2.VariableReadDto;
import step3.infra.feign.FeignClientConfig;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP2", url = "http://localhost:8090", configuration = FeignClientConfig.class)
public interface Step2Proxy {
    @GetMapping("/controllers/{id}")
    ComponentReadDto getControllerById(@PathVariable UUID id);

    @GetMapping("/controllers")
    List<ComponentReadDto> getAllControllers();

    @GetMapping("/states/{id}")
    StateReadDto getStateById(@PathVariable UUID id);

    @GetMapping("/states")
    List<StateReadDto> getAllStates();

    @GetMapping("/variables/{id}")
    VariableReadDto getVariableById(@PathVariable UUID id);

    @GetMapping("/variables")
    List<VariableReadDto> getAllVariables();

    @GetMapping("/control-actions/{id}")
    ControlActionReadDto getControlActionById(@PathVariable UUID id);

    @GetMapping("/control-actions")
    List<ControlActionReadDto> getAllControlActions();
}