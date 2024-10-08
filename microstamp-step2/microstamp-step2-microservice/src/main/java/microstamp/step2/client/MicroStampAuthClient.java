package microstamp.step2.client;

import microstamp.step2.configuration.FeignClientConfiguration;
import microstamp.auth.dto.analysis.AnalysisReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-AUTH-SERVER", configuration = FeignClientConfiguration.class)
public interface MicroStampAuthClient {

    @GetMapping("analyses/{id}")
    AnalysisReadDto getAnalysisById(@PathVariable("id") UUID id);
}
