package microstamp.cast.step5.client;

import microstamp.cast.step5.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-CAST-STEP4", configuration = FeignClientConfiguration.class)
public interface CastStep4Client {

    @GetMapping("systemic-factors/{id}")
    SystemicFactorReadDto readSystemicFactor(@PathVariable("id") UUID id);
}
