package microstamp.step4new.client;

import microstamp.step4new.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-AUTH-SERVER", configuration = FeignClientConfiguration.class)
public interface MicroStampAuthClient {

    @GetMapping("analyses/{id}")
    Object getAnalysisById(@PathVariable("id") UUID id);
}
