package microstamp.cast.step1.entity;

import jakarta.persistence.*;
import lombok.*;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "QuestionRaised")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cast_questions_raised", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "code", "analysis_id" })
})
public class QuestionRaised {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(name = "cast_step", nullable = false)
    private Integer castStep;

    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "analysis_id", nullable = false)
    private UUID analysisId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
