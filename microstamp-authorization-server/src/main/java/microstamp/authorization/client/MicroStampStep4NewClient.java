package microstamp.authorization.client;

import microstamp.authorization.configuration.FeignClientConfiguration;
import microstamp.authorization.dto.step4new.Step4NewExportReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP4-NEW", configuration = FeignClientConfiguration.class)
public interface MicroStampStep4NewClient {

    @GetMapping("export/analysis/{id}/json")
    Step4NewExportReadDto exportStep4NewByAnalysisId(@PathVariable("id") UUID id);

    @GetMapping("export/analysis/{id}/json")
    Step4NewExportReadDto exportStep4NewByAnalysisId(@RequestHeader("Authorization") String authHeader, @PathVariable("id") UUID id);
}
