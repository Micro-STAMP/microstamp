package microstamp.step4new.helper;

import microstamp.step3.dto.UCAType;
import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.constants.FormalScenarioCodes;
import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;
import microstamp.step4new.dto.formalscenarioclass.FormalScenarioClassReadDto;

public class FormalScenarioHelper {

    public static FormalScenarioReadDto buildScenario(UnsafeControlActionFullReadDto uca) {
        String[] templates = getSentences(uca.type());

        for (int i = 0; i < templates.length; i++) {
            templates[i] = templates[i]
                    .replace("<controller>", uca.control_action().connection().source().name())
                    .replace("<controlled-process>", uca.control_action().connection().target().name())
                    .replace("<cmd>", uca.control_action().name())
                    .replace("<context>", uca.context());
        }

        return getFormalScenarioReadDto(templates);
    }

    private static FormalScenarioReadDto getFormalScenarioReadDto(String[] templates) {
        FormalScenarioReadDto dto = new FormalScenarioReadDto();

        dto.setClass1(new FormalScenarioClassReadDto(templates[0], templates[1], FormalScenarioCodes.CLASS1));
        dto.setClass2(new FormalScenarioClassReadDto(templates[2], templates[3], FormalScenarioCodes.CLASS2));
        dto.setClass3(new FormalScenarioClassReadDto(templates[4], templates[5], FormalScenarioCodes.CLASS3));
        dto.setClass4(new FormalScenarioClassReadDto(templates[6], templates[7], FormalScenarioCodes.CLASS4));
        return dto;
    }

    private static String[] getSentences(UCAType ucaType) {
        return switch (ucaType) {
            case NOT_PROVIDED -> new String[]{
                    "<controller> doesn't provide <cmd> when <context>",
                    "<controller> received feedback (or other inputs) that indicates <context>",

                    "Feedback (or other inputs) received by <controller> does not adequately indicate <context>",
                    "<context> is true",

                    "<controller> does provide <cmd> when <context>",
                    "<cmd> is not received by <controlled-process> when <context>",

                    "<cmd> is received by <controlled-process> when <context>",
                    "<controlled-process> does not respond by <...>"
            };
            case PROVIDED -> new String[]{
                    "<controller> provides <cmd> when <context>",
                    "<controller> received feedback (or other inputs) that indicates <context>",

                    "Feedback (or other inputs) received by <controller> does not adequately indicate <context>",
                    "<context> is true",

                    "<controller> does not provide <cmd> when <context>",
                    "<controlled-process> receives <cmd> when <context>",

                    "<cmd> is not received by <controlled-process> when <context>",
                    "<controlled-process> responds by <...>"
            };
            case TOO_EARLY, TOO_LATE, OUT_OF_ORDER -> new String[]{
                    "<controller> provides <cmd> too late/early after/before <context>",
                    "<controller> received feedback (or other inputs) that indicates <context> on time / in order",

                    "Feedback (or other inputs) received by <controller> does not indicate <context> (too late/early/out of order)",
                    "<context> is true",

                    "<controller> does not provide <cmd> when <context> (not too late/early/out of order)",
                    "<cmd> is received by <controlled-process> with <context> (too late/early/out of order)",

                    "<cmd> is not received by <controlled-process> when <context> (not too late/early/out of order)",
                    "<controlled-process> responds by <...> with <context> (too late/early/out of order)"
            };
            case STOPPED_TOO_SOON, APPLIED_TOO_LONG -> new String[]{
                    "<controller> stops/continues providing <cmd> too soon/long",
                    "<controller> received feedback (or other inputs) that indicates <context> on time",

                    "Feedback (or other inputs) received by <controller> does not indicate <context> (inappropriate duration)",
                    "<context> is true",

                    "<controller> provides <cmd> with appropriate duration",
                    "<cmd> is received by <controlled-process> with <context> (inappropriate duration)",

                    "<cmd> is received by <controlled-process> with appropriate duration",
                    "<controlled-process> does not respond by <...> with <context> (inappropriate duration)"
            };
        };
    }
}
