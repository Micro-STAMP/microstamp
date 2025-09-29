package microstamp.step4new.dto.analysis;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisReadDto {

    private UUID id;
    
    private String name;
    
    private String description;
    
    private Instant creationDate;
    
    private UUID userId;
}
