package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Variable")
@Table(name = "variables", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "component_id" }) })
public class Variable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Component component;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "variable_id")
    private List<State> states;

}
