package step3.entity.mit.step2;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State {
    @Id
    private UUID id;
    private String name;

//    @ManyToOne
//    @JoinColumn(name = "variable_id")
//    private Variable variable;
}
