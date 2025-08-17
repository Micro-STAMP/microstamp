package microstamp.step4new.mapper;

import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.constants.FormalScenarioCodes;
import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;
import microstamp.step4new.dto.formalscenarioclass.FormalScenarioClassReadDto;
import microstamp.step4new.entity.FormalScenarioClass;
import microstamp.step4new.helper.FormalScenarioHelper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class FormalScenarioMapper {

    private FormalScenarioMapper() {}

    public static FormalScenarioReadDto toReadDto(UnsafeControlActionFullReadDto uca,
                                                  List<FormalScenarioClass> classes) {
        String[] templates = FormalScenarioHelper.generateTemplates(uca);

        Map<String, UUID> classIdByCode = classes == null
                ? Map.of()
                : classes.stream().collect(Collectors.toMap(FormalScenarioClass::getCode, FormalScenarioClass::getId));

        FormalScenarioReadDto dto = new FormalScenarioReadDto();

        dto.setClass1(FormalScenarioClassReadDto.builder()
                .id(classIdByCode.get(FormalScenarioCodes.CLASS1))
                .output(templates[0])
                .input(templates[1])
                .code(FormalScenarioCodes.CLASS1)
                .build());
        dto.setClass2(FormalScenarioClassReadDto.builder()
                .id(classIdByCode.get(FormalScenarioCodes.CLASS2))
                .output(templates[2])
                .input(templates[3])
                .code(FormalScenarioCodes.CLASS2)
                .build());
        dto.setClass3(FormalScenarioClassReadDto.builder()
                .id(classIdByCode.get(FormalScenarioCodes.CLASS3))
                .output(templates[4])
                .input(templates[5])
                .code(FormalScenarioCodes.CLASS3)
                .build());
        dto.setClass4(FormalScenarioClassReadDto.builder()
                .id(classIdByCode.get(FormalScenarioCodes.CLASS4))
                .output(templates[6])
                .input(templates[7])
                .code(FormalScenarioCodes.CLASS4)
                .build());

        return dto;
    }
}