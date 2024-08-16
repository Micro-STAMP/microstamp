package step3.entity.mit.step2;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variable {
    private UUID id;
    private String name;
    private List<State> states = new ArrayList<>();
}
