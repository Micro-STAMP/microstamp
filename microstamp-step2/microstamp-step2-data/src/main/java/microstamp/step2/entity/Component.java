package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.step2.enumeration.Style;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Component")
@Table(name = "components", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "analysis_id" }) })
public abstract class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String name;

    private Boolean isVisible;

    private Style border;

    @ManyToOne
    @JoinColumn(name = "father_id")
    private Component father;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "component_id")
    private List<Variable> variables;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "component_id")
    private List<Responsibility> responsibilities;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

}
