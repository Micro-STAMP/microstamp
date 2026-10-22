package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.step2.enumeration.Style;
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
@Entity(name = "Connection")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "connections", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "analysis_id" }) })
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Component source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Component target;

    private Style style;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "connection_id")
    private List<Interaction> interactions;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

}
