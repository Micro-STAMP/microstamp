package microstamp.authorization.dto;

import lombok.*;
import microstamp.authorization.dto.step1.Step1ExportReadDto;
import microstamp.authorization.dto.step2.Step2ExportReadDto;
import microstamp.authorization.dto.step3.Step3ExportReadDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportReadDto {

    private AnalysisReadDto analysis;

    private Step1ExportReadDto step1;

    private Step2ExportReadDto step2;

    private Step3ExportReadDto step3;
}
