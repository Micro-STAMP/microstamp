package microstamp.cast.step5.client;

import microstamp.cast.step5.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-CAST-STEP3", configuration = FeignClientConfiguration.class)
public interface CastStep3Client {

    @GetMapping("inadequate-control-actions/{id}")
    InadequateControlActionReadDto readInadequateControlAction(@PathVariable("id") UUID id);
}
