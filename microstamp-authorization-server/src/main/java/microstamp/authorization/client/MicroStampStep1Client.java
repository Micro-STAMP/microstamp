package microstamp.authorization.client;

import microstamp.authorization.configuration.FeignClientConfiguration;
import microstamp.authorization.dto.step1.Step1ExportReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP1", configuration = FeignClientConfiguration.class)
public interface MicroStampStep1Client {

    @GetMapping("export/analysis/{id}/json")
    Step1ExportReadDto exportStep1ByAnalysisId(@PathVariable("id") UUID id);

    @GetMapping("export/analysis/{id}/json")
    Step1ExportReadDto exportStep1ByAnalysisId(@RequestHeader("Authorization") String authHeader, @PathVariable("id") UUID id);
}
