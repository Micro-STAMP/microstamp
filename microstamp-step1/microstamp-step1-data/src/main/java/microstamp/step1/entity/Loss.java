package microstamp.step1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.sql.Types;
import java.util.UUID;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Loss")
@Table(name = "losses", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "analysis_id" }) })
public class Loss implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String name;

    private String code;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

}
