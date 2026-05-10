package microstamp.cast.step1.client;

import microstamp.cast.step1.dto.analysis.AnalysisReadDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
@Component
public interface MicroStampClient {

    @GetMapping("/analyses/{id}")
    AnalysisReadDto getAnalysisById(@PathVariable("id") UUID id);
}
