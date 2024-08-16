package microstamp.step2.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import microstamp.step2.enumeration.Style;

import java.util.UUID;

@Entity(name = "Environment")
public class Environment extends Component {

    @Builder
    public Environment(UUID analysisId) {
        setName("Environment");
        setCode("Env");
        setIsVisible(true);
        setBorder(Style.SOLID);
        setFather(null);
        setAnalysisId(analysisId);
    }
}
