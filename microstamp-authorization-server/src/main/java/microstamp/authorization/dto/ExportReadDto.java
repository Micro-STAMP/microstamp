package microstamp.authorization.dto;

import lombok.*;
import microstamp.authorization.dto.step1.Step1ExportReadDto;
import microstamp.authorization.dto.step2.Step2ExportReadDto;
import microstamp.authorization.dto.step3.Step3ExportReadDto;
import microstamp.authorization.dto.step4.Step4ExportReadDto;
import microstamp.authorization.dto.step4new.Step4NewExportReadDto;

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

    private Step4ExportReadDto step4;

    private Step4NewExportReadDto step4new;
}
