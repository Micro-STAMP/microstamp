package microstamp.step4new.helper;

import microstamp.step2.dto.VariableReadDto;
import microstamp.step3.dto.UnsafeControlActionFullReadDto;

import java.util.List;
import java.util.stream.Collectors;

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

		if (result.contains("PM-1"))
			result = result.replace("PM-1", formatVariables(uca));

		return result;
	}

	private static String formatVariables(UnsafeControlActionFullReadDto uca) {
		if (uca.control_action() == null
			|| uca.control_action().connection() == null
			|| uca.control_action().connection().source() == null
			|| uca.control_action().connection().source().variables() == null) {
			return "PM-1";
		}

		List<VariableReadDto> variables = uca.control_action().connection().source().variables();
		
		if (variables.isEmpty())
			return "PM-1";

		String variableNames = variables.stream()
				.map(VariableReadDto::name)
				.collect(Collectors.joining(" | "));
		
		return "(" + variableNames + ")";
	}
}