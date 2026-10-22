package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Variable")
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

}
