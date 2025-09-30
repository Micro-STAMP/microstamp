import { IFormalScenariosReadDto } from "@interfaces/IStep4New/IFormalScenarios";
import { IHighLevelSolutionsReadDto } from "./IHighLevelSolutionsReadDto";

/* - - - - - - - - - - - - - - - - - - - - - - */
// * High Level Solutions by Class DTO

interface IHighLevelSolutionsByClass {
	class1: IHighLevelSolutionsReadDto;
	class2: IHighLevelSolutionsReadDto;
	class3: IHighLevelSolutionsReadDto;
	class4: IHighLevelSolutionsReadDto;
}

export type { IHighLevelSolutionsByClass };

/* - - - - - - - - - - - - - - - - - - - - - - */
// * Handle Group High Level Solutions by Class

function groupHighLevelSolutionsByClass(
	solutions?: IHighLevelSolutionsReadDto[],
	formalScenarios?: IFormalScenariosReadDto
) {
	if (!solutions || !formalScenarios) return undefined;
	const getSolutionByClassId = (solutions: IHighLevelSolutionsReadDto[], classId: string) => {
		return solutions.find(solution => solution.formalScenarioClassId === classId);
	};
	const result: IHighLevelSolutionsByClass = {
		class1: getSolutionByClassId(solutions, formalScenarios.class1.id)!,
		class2: getSolutionByClassId(solutions, formalScenarios.class2.id)!,
		class3: getSolutionByClassId(solutions, formalScenarios.class3.id)!,
		class4: getSolutionByClassId(solutions, formalScenarios.class4.id)!
	};

	return result;
}

export { groupHighLevelSolutionsByClass };

/* - - - - - - - - - - - - - - - - - - - - - - */
