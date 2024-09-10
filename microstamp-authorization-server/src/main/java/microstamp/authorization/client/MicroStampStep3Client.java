package microstamp.authorization.client;

import microstamp.authorization.configuration.FeignClientConfiguration;
import microstamp.authorization.dto.step3.Step3ExportReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP3", configuration = FeignClientConfiguration.class)
public interface MicroStampStep3Client {

    @GetMapping("export/analysis/{id}/json")
    Step3ExportReadDto exportStep3ByAnalysisId(@PathVariable("id") UUID id);
}
