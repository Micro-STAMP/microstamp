package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.step2.enumeration.InteractionType;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Interaction")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "interactions", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "connection_id" }) })
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String name;

    private InteractionType interactionType;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private Connection connection;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

}
