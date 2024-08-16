package microstamp.step1.client;

import microstamp.step1.configuration.FeignClientConfiguration;
import microstamp.step1.dto.analysis.AnalysisReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-AUTH-SERVER", configuration = FeignClientConfiguration.class)
public interface MicroStampClient {

    @GetMapping("analyses/{id}")
    AnalysisReadDto getAnalysisById(@PathVariable("id") UUID id);
}
