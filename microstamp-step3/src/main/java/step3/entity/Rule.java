package step3.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.entity.association.RuleState;

import java.sql.Types;
import java.util.*;

@Table(name = "rule")
@Entity(name = "Rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rule {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;
    private String name;
    private String code;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID controlActionId;

    @OneToMany(
            mappedBy = "rule",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RuleState> stateAssociations = new ArrayList<>();

    @JdbcTypeCode(Types.VARCHAR)
    private UUID hazardId;

    @ElementCollection(targetClass = UCAType.class)
    @CollectionTable(name = "rule_type", joinColumns = @JoinColumn(name = "rule_id"))
    @Enumerated(EnumType.STRING) @Column(name = "type")
    @Builder.Default
    private Set<UCAType> types = new HashSet<>();

    public void addType(UCAType type) {
        types.add(type);
    }

}
