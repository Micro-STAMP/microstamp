import Container from "@components/Container";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { getFormalClassTitle, IFormalScenariosClass } from "@interfaces/IStep4New/Enums";
import { IFormalScenarioClassDto } from "@interfaces/IStep4New/IFormalScenarios";
import { IHighLevelSolutionsReadDto } from "@interfaces/IStep4New/IHighLevelSolutions";
import { IRefinedScenarioReadDto } from "@interfaces/IStep4New/IRefinedScenarios";
import { IRefinedSolutionReadDto } from "@interfaces/IStep4New/IRefinedSolutions";
import { ContainerActivityLayout } from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/components/class";
import {
	HighLevelScenarioContent,
	HighLevelSolutionContent,
	RefinedScenarioContent,
	RefinedSolutionContent
} from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/components/contents";

interface ClassContainerProps {
	uca: IUnsafeControlActionReadDto;
	formalClass: IFormalScenariosClass;
	formalScenarioByClass?: IFormalScenarioClassDto;
	highLevelSolutionByClass?: IHighLevelSolutionsReadDto;
	refinedScenariosByClass?: IRefinedScenarioReadDto[];
	refinedSolutionsByClass?: IRefinedSolutionReadDto[];
	isLoading?: boolean;
	isError?: boolean;
}
function ClassContainer({
	uca,
	formalClass,
	formalScenarioByClass,
	highLevelSolutionByClass,
	refinedScenariosByClass,
	refinedSolutionsByClass,
	isLoading,
	isError
}: ClassContainerProps) {
	return (
		<Container
			title={getFormalClassTitle(formalClass)}
			collapsible
			justTitle
			isLoading={isLoading}
			isError={isError}
		>
			{formalScenarioByClass &&
				highLevelSolutionByClass &&
				refinedScenariosByClass &&
				refinedSolutionsByClass && (
					<ContainerActivityLayout
						activity1Content={
							<HighLevelScenarioContent formalScenario={formalScenarioByClass} />
						}
						activity2Content={
							<HighLevelSolutionContent
								formalScenario={formalScenarioByClass}
								solution={highLevelSolutionByClass}
							/>
						}
						activity3Content={
							<RefinedScenarioContent
								uca={uca}
								scenarios={refinedScenariosByClass}
								formalScenarioClassId={formalScenarioByClass.id}
							/>
						}
						activity4Content={
							<RefinedSolutionContent
								uca={uca}
								refinedSolutionsByClass={refinedSolutionsByClass}
								refinedScenariosByClass={refinedScenariosByClass}
							/>
						}
					/>
				)}
		</Container>
	);
}

export default ClassContainer;
