package microstamp.cast.step1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AccidentLossEvent")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cast_accident_loss_events", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "code", "analysis_id" })
})
public class AccidentLossEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
