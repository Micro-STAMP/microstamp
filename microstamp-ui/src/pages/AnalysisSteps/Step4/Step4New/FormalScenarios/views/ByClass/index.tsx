import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { getFormalClassKey, IFormalScenariosClass } from "@interfaces/IStep4New/Enums";
import { IFormalScenariosReadDto } from "@interfaces/IStep4New/IFormalScenarios";
import {
	groupHighLevelSolutionsByClass,
	IHighLevelSolutionsReadDto
} from "@interfaces/IStep4New/IHighLevelSolutions";
import {
	groupRefinedScenariosByClass,
	IRefinedScenarioReadDto
} from "@interfaces/IStep4New/IRefinedScenarios";
import {
	groupRefinedSolutionsByClass,
	IRefinedSolutionReadDto
} from "@interfaces/IStep4New/IRefinedSolutions";
import { ClassContainer } from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/components/class";

interface FormalScenariosByActivityProps {
	uca: IUnsafeControlActionReadDto;
	formalScenarios?: IFormalScenariosReadDto;
	highLevelSolutions?: IHighLevelSolutionsReadDto[];
	refinedScenarios?: IRefinedScenarioReadDto[];
	refinedSolutions?: IRefinedSolutionReadDto[];
	isLoading?: boolean;
	isError?: boolean;
}
function FormalScenariosByClass({
	uca,
	formalScenarios,
	highLevelSolutions,
	refinedScenarios,
	refinedSolutions,
	isLoading,
	isError
}: FormalScenariosByActivityProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Group By Class

	// High Level Solutions By Class
	const highLevelSolutionsByClass = groupHighLevelSolutionsByClass(
		highLevelSolutions,
		formalScenarios
	);

	// Refined Scenarios By Class
	const refinedScenariosByClass = groupRefinedScenariosByClass(refinedScenarios, formalScenarios);

	// Refined Solutions By Class
	const refinedSolutionsByClass = groupRefinedSolutionsByClass(
		refinedSolutions,
		refinedScenariosByClass
	);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	const formalClasses = [
		IFormalScenariosClass.CLASS_1,
		IFormalScenariosClass.CLASS_2,
		IFormalScenariosClass.CLASS_3,
		IFormalScenariosClass.CLASS_4
	];
	return (
		<>
			{formalClasses.map(formalClass => (
				<ClassContainer
					key={formalClass.toString()}
					formalClass={formalClass}
					uca={uca}
					formalScenarioByClass={formalScenarios?.[getFormalClassKey(formalClass)]}
					highLevelSolutionByClass={
						highLevelSolutionsByClass?.[getFormalClassKey(formalClass)]
					}
					refinedScenariosByClass={
						refinedScenariosByClass?.[getFormalClassKey(formalClass)]
					}
					refinedSolutionsByClass={
						refinedSolutionsByClass?.[getFormalClassKey(formalClass)]
					}
					isLoading={isLoading}
					isError={isError}
				/>
			))}
		</>
	);
}

export default FormalScenariosByClass;
