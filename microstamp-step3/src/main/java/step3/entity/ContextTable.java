package step3.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "context_table")
@Entity(name = "ContextTable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContextTable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @OneToMany(
            mappedBy = "contextTable",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Context> contexts = new ArrayList<>();

    @JdbcTypeCode(Types.VARCHAR)
    private UUID controlActionId;

    public void addContext(Context context) {
        this.contexts.add(context);
        context.setContextTable(this);
    }
}
