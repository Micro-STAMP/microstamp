import { IFormalScenariosReadDto } from "@interfaces/IStep4New/IFormalScenarios";
import { IRefinedScenarioReadDto } from "./IRefinedScenarioReadDto";

/* - - - - - - - - - - - - - - - - - - - - - - */
// * Handle Group Refined Scenarios by Class

interface IRefinedScenarioByClass {
	class1: IRefinedScenarioReadDto[];
	class2: IRefinedScenarioReadDto[];
	class3: IRefinedScenarioReadDto[];
	class4: IRefinedScenarioReadDto[];
}

export type { IRefinedScenarioByClass };

/* - - - - - - - - - - - - - - - - - - - - - - */
// * Handle Group Refined Scenarios by Class

function groupRefinedScenariosByClass(
	refinedScenarios: IRefinedScenarioReadDto[] | undefined,
	formalScenarios: IFormalScenariosReadDto
) {
	const result: IRefinedScenarioByClass = {
		class1: [],
		class2: [],
		class3: [],
		class4: []
	};

	if (!refinedScenarios) return result;
	refinedScenarios.forEach(scenario => {
		if (scenario.formalScenarioClassId === formalScenarios.class1.id) {
			result.class1.push(scenario);
		} else if (scenario.formalScenarioClassId === formalScenarios.class2.id) {
			result.class2.push(scenario);
		} else if (scenario.formalScenarioClassId === formalScenarios.class3.id) {
			result.class3.push(scenario);
		} else if (scenario.formalScenarioClassId === formalScenarios.class4.id) {
			result.class4.push(scenario);
		}
	});

	return result;
}

export { groupRefinedScenariosByClass };

/* - - - - - - - - - - - - - - - - - - - - - - */
