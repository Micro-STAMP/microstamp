import { IRefinedScenarioByClass } from "@interfaces/IStep4New/IRefinedScenarios";
import { IRefinedSolutionReadDto } from "./IRefinedSolutionReadDto";

/* - - - - - - - - - - - - - - - - - - - - - - */
// * Handle Group Refined Solutions by Class

interface IRefinedSolutionsByClass {
	class1: IRefinedSolutionReadDto[];
	class2: IRefinedSolutionReadDto[];
	class3: IRefinedSolutionReadDto[];
	class4: IRefinedSolutionReadDto[];
}

export type { IRefinedSolutionsByClass };

/* - - - - - - - - - - - - - - - - - - - - - - */
// * Handle Group Refined Solutionss by Class

function groupRefinedSolutionsByClass(
	refinedSolutions: IRefinedSolutionReadDto[] | undefined,
	groupedRefinedScenarios: IRefinedScenarioByClass
) {
	const result: IRefinedSolutionsByClass = {
		class1: [],
		class2: [],
		class3: [],
		class4: []
	};

	if (!refinedSolutions) return result;

	const scenarioIdToClass = new Map<string, keyof IRefinedSolutionsByClass>();
	(["class1", "class2", "class3", "class4"] as const).forEach(classKey => {
		groupedRefinedScenarios[classKey].forEach(scenario => {
			scenarioIdToClass.set(scenario.id, classKey);
		});
	});
	refinedSolutions.forEach(solution => {
		const classKey = scenarioIdToClass.get(solution.refinedScenarioId);
		if (classKey) {
			result[classKey].push(solution);
		}
	});

	return result;
}

export { groupRefinedSolutionsByClass };

/* - - - - - - - - - - - - - - - - - - - - - - */
