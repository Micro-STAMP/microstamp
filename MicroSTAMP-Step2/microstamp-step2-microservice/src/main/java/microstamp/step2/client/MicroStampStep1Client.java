package microstamp.step2.client;

import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step2.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-STEP1", configuration = FeignClientConfiguration.class)
public interface MicroStampStep1Client {

    @GetMapping("system-safety-constraints/{id}")
    SystemSafetyConstraintReadDto getSystemSafetyConstraintById(@PathVariable("id") UUID id);
}
