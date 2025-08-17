package microstamp.authorization.client;

import microstamp.authorization.configuration.FeignClientConfiguration;
import microstamp.authorization.dto.step4.Step4ExportReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP4", configuration = FeignClientConfiguration.class)
public interface MicroStampStep4Client {

    @GetMapping("export/analysis/{id}/json")
    Step4ExportReadDto exportStep4ByAnalysisId(@PathVariable("id") UUID id);

    @GetMapping("export/analysis/{id}/json")
    Step4ExportReadDto exportStep4ByAnalysisId(@RequestHeader("Authorization") String authHeader, @PathVariable("id") UUID id);
}
