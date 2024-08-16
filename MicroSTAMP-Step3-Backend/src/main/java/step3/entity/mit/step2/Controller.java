package step3.entity.mit.step2;

import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Controller {
    @Id
    private UUID id;
    private UUID projectId;
    private String name;
    private List<ControlAction> controlActions = new ArrayList<>();
    private List<Variable> variables = new ArrayList<>();
//    private ContextTable contextTable;
}
