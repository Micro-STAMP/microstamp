package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Responsibility")
@Table(name = "responsibilities")
public class Responsibility {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String responsibility;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Component component;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID systemSafetyConstraintId;

}
