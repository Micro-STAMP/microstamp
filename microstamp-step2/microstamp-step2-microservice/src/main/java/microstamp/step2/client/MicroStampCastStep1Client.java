package microstamp.step2.client;

import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.step2.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-CAST-STEP1", configuration = FeignClientConfiguration.class)
public interface MicroStampCastStep1Client {

    @GetMapping("cast/violated-system-safety-constraints/{id}")
    ViolatedSystemSafetyConstraintReadDto getViolatedSystemSafetyConstraintById(@PathVariable("id") UUID id);
}
