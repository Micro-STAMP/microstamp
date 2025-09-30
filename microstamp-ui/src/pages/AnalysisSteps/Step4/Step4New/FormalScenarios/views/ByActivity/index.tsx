import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { IFormalScenariosActivity } from "@interfaces/IStep4New/Enums";
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
import { ActivityContainer } from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/components/activity";

interface FormalScenariosByActivityProps {
	uca: IUnsafeControlActionReadDto;
	formalScenarios?: IFormalScenariosReadDto;
	highLevelSolutions?: IHighLevelSolutionsReadDto[];
	refinedScenarios?: IRefinedScenarioReadDto[];
	refinedSolutions?: IRefinedSolutionReadDto[];
	isLoading?: boolean;
	isError?: boolean;
}
function FormalScenariosByActivity({
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

	const formalActivities = [
		IFormalScenariosActivity.HIGH_LEVEL_SCENARIOS,
		IFormalScenariosActivity.HIGH_LEVEL_SOLUTIONS,
		IFormalScenariosActivity.REFINED_SCENARIOS,
		IFormalScenariosActivity.REFINED_SOLUTIONS
	];
	return (
		<>
			{formalActivities.map(formalActivity => (
				<ActivityContainer
					key={formalActivity.toString()}
					formalActivity={formalActivity}
					uca={uca}
					groupedFormalScenario={formalScenarios}
					groupedHighLevelSolution={highLevelSolutionsByClass}
					groupedRefinedScenarios={refinedScenariosByClass}
					groupedRefinedSolutions={refinedSolutionsByClass}
					isLoading={isLoading}
					isError={isError}
				/>
			))}
		</>
	);
}

export default FormalScenariosByActivity;
