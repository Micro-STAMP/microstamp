package step3.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import step3.dto.mit.step1.HazardReadDto;
import step3.infra.feign.FeignClientConfig;

import java.util.List;

@FeignClient(name = "MICROSTAMP-STEP1", configuration = FeignClientConfig.class)
public interface Step1Proxy {
    @GetMapping("/hazards")
    List<HazardReadDto> getHazards();
}
