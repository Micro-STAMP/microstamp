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
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CastHazard")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cast_hazards", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "code", "analysis_id" })
})
public class CastHazard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cast_hazard_accident_loss_event",
            joinColumns = @JoinColumn(name = "hazard_id"),
            inverseJoinColumns = @JoinColumn(name = "accident_loss_event_id")
    )
    private List<AccidentLossEvent> accidentLossEvents;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}