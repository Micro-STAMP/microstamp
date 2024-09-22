package step3.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import step3.dto.auth.AnalysisReadDto;
import step3.infra.config.feign.FeignClientConfig;

import java.util.UUID;

@FeignClient(name = "MICROSTAMP-AUTH-SERVER", configuration = FeignClientConfig.class)
public interface AuthServerProxy {
    @GetMapping("analyses/{id}")
    AnalysisReadDto getAnalysisById(@PathVariable UUID id);
}
