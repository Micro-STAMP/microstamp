import Container from "@components/Container";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import {
	getFormalActivityTitle,
	getFormalClassKey,
	IFormalScenariosActivity,
	IFormalScenariosClass
} from "@interfaces/IStep4New/Enums";
import { IFormalScenariosReadDto } from "@interfaces/IStep4New/IFormalScenarios";
import { IHighLevelSolutionsByClass } from "@interfaces/IStep4New/IHighLevelSolutions";
import { IRefinedScenarioByClass } from "@interfaces/IStep4New/IRefinedScenarios";
import { IRefinedSolutionsByClass } from "@interfaces/IStep4New/IRefinedSolutions";
import { ContainerClassLayout } from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/components/activity";
import {
	HighLevelScenarioContent,
	HighLevelSolutionContent,
	RefinedScenarioContent,
	RefinedSolutionContent
} from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/components/contents";

interface ActivityContainerProps {
	uca: IUnsafeControlActionReadDto;
	formalActivity: IFormalScenariosActivity;
	groupedFormalScenario?: IFormalScenariosReadDto;
	groupedHighLevelSolution?: IHighLevelSolutionsByClass;
	groupedRefinedScenarios?: IRefinedScenarioByClass;
	groupedRefinedSolutions?: IRefinedSolutionsByClass;
	isLoading?: boolean;
	isError?: boolean;
}
function ActivityContainer({
	uca,
	formalActivity,
	groupedFormalScenario,
	groupedHighLevelSolution,
	groupedRefinedScenarios,
	groupedRefinedSolutions,
	isLoading,
	isError
}: ActivityContainerProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Class Content

	const classContent = (
		formalClass: IFormalScenariosClass,
		groupedFormalScenario: IFormalScenariosReadDto,
		groupedHighLevelSolution: IHighLevelSolutionsByClass,
		groupedRefinedScenarios: IRefinedScenarioByClass,
		groupedRefinedSolutions: IRefinedSolutionsByClass
	) => {
		const classKey = getFormalClassKey(formalClass);
		switch (formalActivity) {
			case IFormalScenariosActivity.HIGH_LEVEL_SCENARIOS:
				return (
					<HighLevelScenarioContent formalScenario={groupedFormalScenario[classKey]} />
				);
			case IFormalScenariosActivity.HIGH_LEVEL_SOLUTIONS:
				return (
					<HighLevelSolutionContent
						formalScenario={groupedFormalScenario[classKey]}
						solution={groupedHighLevelSolution[classKey]}
					/>
				);
			case IFormalScenariosActivity.REFINED_SCENARIOS:
				return (
					<RefinedScenarioContent
						uca={uca}
						formalScenarioClassId={groupedFormalScenario[classKey].id}
						scenarios={groupedRefinedScenarios[classKey]}
					/>
				);
			case IFormalScenariosActivity.REFINED_SOLUTIONS:
				return (
					<RefinedSolutionContent
						uca={uca}
						refinedSolutionsByClass={groupedRefinedSolutions[classKey]}
						refinedScenariosByClass={groupedRefinedScenarios[classKey]}
					/>
				);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<Container
			title={getFormalActivityTitle(formalActivity)}
			collapsible
			justTitle
			isLoading={isLoading}
			isError={isError}
		>
			{groupedFormalScenario &&
				groupedHighLevelSolution &&
				groupedRefinedScenarios &&
				groupedRefinedSolutions && (
					<ContainerClassLayout
						class1Content={classContent(
							IFormalScenariosClass.CLASS_1,
							groupedFormalScenario,
							groupedHighLevelSolution,
							groupedRefinedScenarios,
							groupedRefinedSolutions
						)}
						class2Content={classContent(
							IFormalScenariosClass.CLASS_2,
							groupedFormalScenario,
							groupedHighLevelSolution,
							groupedRefinedScenarios,
							groupedRefinedSolutions
						)}
						class3Content={classContent(
							IFormalScenariosClass.CLASS_3,
							groupedFormalScenario,
							groupedHighLevelSolution,
							groupedRefinedScenarios,
							groupedRefinedSolutions
						)}
						class4Content={classContent(
							IFormalScenariosClass.CLASS_4,
							groupedFormalScenario,
							groupedHighLevelSolution,
							groupedRefinedScenarios,
							groupedRefinedSolutions
						)}
					/>
				)}
		</Container>
	);
}

export default ActivityContainer;
