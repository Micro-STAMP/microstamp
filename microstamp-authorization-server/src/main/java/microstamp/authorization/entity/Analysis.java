package microstamp.authorization.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Entity(name = "Analysis")
@Table(name = "analyses")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String name;

    private String description;

    @CreatedDate
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Analysis(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
