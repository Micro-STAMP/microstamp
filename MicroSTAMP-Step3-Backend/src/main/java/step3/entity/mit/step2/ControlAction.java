package step3.entity.mit.step2;

import jakarta.persistence.*;
import lombok.*;
import step3.entity.Controller;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControlAction {
    @Id
    private UUID id;
    private UUID controllerId;
    private String name;
}
