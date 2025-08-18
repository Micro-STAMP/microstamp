package microstamp.step4new.helper;

import microstamp.step3.dto.UnsafeControlActionFullReadDto;

public class RefinedScenarioHelper {

	public static String applyTemplate(String template, UnsafeControlActionFullReadDto uca) {
		if (template == null || uca == null || uca.control_action() == null || uca.control_action().connection() == null)
			return template;

		String controllerName = uca.control_action().connection().source() != null
				? uca.control_action().connection().source().name() : null;
		String controlActionName = uca.control_action().name();

		String result = template;
		if (controllerName != null)
			result = result.replace("<controller>", controllerName);

		if (controlActionName != null)
			result = result.replace("<control-action>", controlActionName);

		return result;
	}
}