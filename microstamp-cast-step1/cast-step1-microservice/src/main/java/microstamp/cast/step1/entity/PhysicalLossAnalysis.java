package microstamp.cast.step1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PhysicalLossAnalysis")
@Table(name = "cast_physical_loss_analyses", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "code", "analysis_id" })
})
public class PhysicalLossAnalysis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    //Ver amanhã certinho o que manter e o que tirar, pra manter a análise alinhada com o livro
    //Talvez adicionar uma conexão aqui com os acidentes em si, pra identificar o que causou cada perda? Uma explosão
    //e um incêndio podem ocorrer e causar diferentes perdas. Ajudaria a levantar as questões certas.
    @Column(columnDefinition = "TEXT")
    private String physicalLossDescription;

    @Column(columnDefinition = "TEXT")
    private String affectedEquipment;

    @Column(columnDefinition = "TEXT")
    private String physicalDesignRequirements;

    @Column(columnDefinition = "TEXT")
    private String physicalControls;

    @Column(columnDefinition = "TEXT")
    private String failuresAndUnsafeInteractions;

    @Column(columnDefinition = "TEXT")
    private String missingOrInadequateControls;

    @Column(columnDefinition = "TEXT")
    private String contextualFactors;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;
}