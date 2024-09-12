package step3.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import step3.dto.step1.HazardReadDto;
import step3.infra.feign.FeignClientConfig;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP1", url = "http://localhost:8091", configuration = FeignClientConfig.class)
public interface Step1Proxy {
    @GetMapping("/hazards")
    List<HazardReadDto> getHazards();

    @GetMapping("/hazards/{id}")
    HazardReadDto getHazardById(@PathVariable UUID id);
}
