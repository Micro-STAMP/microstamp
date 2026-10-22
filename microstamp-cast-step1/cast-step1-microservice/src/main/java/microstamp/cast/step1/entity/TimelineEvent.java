package microstamp.cast.step1.entity;

import jakarta.persistence.*;
import lombok.*;
import microstamp.cast.step1.converter.StringListConverter;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Types;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TimelineEvent")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cast_timeline_events", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "code", "analysis_id" })
})
public class TimelineEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private Integer eventOrder;

    @Column(columnDefinition = "TEXT")
    private String eventDescription;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> questions;

    private String timeLabel;

    private Instant exactDateTime;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    public String getImageBase64() {
        return image == null ? null : Base64.getEncoder().encodeToString(image);
    }
}