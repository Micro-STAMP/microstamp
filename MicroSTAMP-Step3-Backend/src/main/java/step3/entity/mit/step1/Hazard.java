package step3.entity.mit.step1;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hazard {
    @Id
    private UUID id;
    private String name;
    private String code;
}
