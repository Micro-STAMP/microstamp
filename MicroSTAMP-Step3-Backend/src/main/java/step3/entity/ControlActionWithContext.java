package step3.entity;

import lombok.*;

// TODO Talvez seja uma classe
@Getter @Setter @NoArgsConstructor
public class ControlActionWithContext {
    private Long id;
    private ControlAction controlAction;
    private Context context;
    private UCAType type;
    private Boolean unsafe = false;

    // Constructors -----------------------------------

    // Methods ----------------------------------------

    // ------------------------------------------------
}
