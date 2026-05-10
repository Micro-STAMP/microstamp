package microstamp.cast.step1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

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
@Entity(name = "TimelineEvent")
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

    //Decidir se compensa adicionar isso aqui, mas eu acho que não, encheção de linguiça desnecessária
    //private Instant eventDate;

    @Column(columnDefinition = "TEXT")
    private String eventDescription;

    @Column(columnDefinition = "TEXT")
    private String questions;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;
}