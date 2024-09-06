package microstamp.authorization.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Base64;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Image")
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String fileName;

    @Lob
    @Column(columnDefinition="LONGBLOB")
    private byte[] data;

    @OneToOne(mappedBy = "image")
    private Analysis analysis;

    public String getBase64() {
        return Base64.getEncoder().encodeToString(data);
    }

}
