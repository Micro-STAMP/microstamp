package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.step2.enumeration.ConnectionActionType;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ConnectionAction")
@Table(name = "connectionActions", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "connection_id" }) })
public class ConnectionAction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String name;

    private ConnectionActionType connectionActionType;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private Connection connection;

}
