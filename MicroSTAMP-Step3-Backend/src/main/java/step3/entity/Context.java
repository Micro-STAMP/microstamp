package step3.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Table(name = "context")
@Entity(name = "Context")
@Getter @Setter @NoArgsConstructor
public class Context {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "context_value",
        joinColumns = @JoinColumn(name = "context_id"),
        inverseJoinColumns = @JoinColumn(name = "value_id")
    )
    private List<Value> values = new ArrayList<>();

    @ManyToOne @JoinColumn(name = "context_table_id")
    private ContextTable contextTable;

    // Constructors -----------------------------------

    public Context(List<Value> values) {
        this.values = values;
    }

    // Methods ----------------------------------------

    @Override
    public String toString() {
        StringJoiner context = new StringJoiner(" AND ");
        for (Value value : values) {
            context.add(value.toString());
        }
        return context.toString();
    }

    // ------------------------------------------------
}
