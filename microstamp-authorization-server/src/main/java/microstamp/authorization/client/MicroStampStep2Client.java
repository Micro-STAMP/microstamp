package microstamp.authorization.client;

import microstamp.authorization.configuration.FeignClientConfiguration;
import microstamp.authorization.dto.step2.Step2ExportReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP2", configuration = FeignClientConfiguration.class)
public interface MicroStampStep2Client {

    @GetMapping("export/analysis/{id}/json")
    Step2ExportReadDto exportStep2ByAnalysisId(@PathVariable("id") UUID id);
}
