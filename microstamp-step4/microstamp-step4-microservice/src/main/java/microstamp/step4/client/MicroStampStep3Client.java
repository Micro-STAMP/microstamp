package microstamp.step4.client;

import microstamp.step3.dto.unsafecontrolaction.UnsafeControlActionReadDto;
import microstamp.step4.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP3", configuration = FeignClientConfiguration.class)
public interface MicroStampStep3Client {

    @GetMapping("unsafe-control-action/{id}")
    UnsafeControlActionReadDto readUnsafeControlAction(@PathVariable("id") UUID id);

    @GetMapping("unsafe-control-action/analysis/{id}")
    List<UnsafeControlActionReadDto> readAllUCAByAnalysisId(@PathVariable("id") UUID analysisId);
}
