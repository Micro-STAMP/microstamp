package microstamp.step4new.client;

import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP3", configuration = FeignClientConfiguration.class)
public interface MicroStampStep3Client {

    @GetMapping("unsafe-control-action/full/{id}")
    UnsafeControlActionFullReadDto readUnsafeControlAction(@PathVariable("id") UUID id);
}
