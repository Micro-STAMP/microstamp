import Button from "@components/Button";
import Container from "@components/Container";
import { getHighLevelSolutionsByUCA } from "@http/Step4New/HighLevelSolutions";
import { IFormalScenariosReadDto } from "@interfaces/IStep4New/IFormalScenarios";
import ContainerClassLayout from "@pages/AnalysisSteps/Step4/Step4New/components/ContainerClassLayout";
import { useQuery } from "@tanstack/react-query";
import {
	TbCircleDashedNumber1,
	TbCircleDashedNumber2,
	TbCircleDashedNumber3,
	TbCircleDashedNumber4
} from "react-icons/tb";
import styles from "./HighLevelSolutionsContainer.module.css";

interface HighLevelSolutionsContainerProps {
	formalScenarios?: IFormalScenariosReadDto;
	ucaId: string;
}
function HighLevelSolutionsContainer({ formalScenarios, ucaId }: HighLevelSolutionsContainerProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get High Level Scenarios

	const {
		data: solutions,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["high-level-solutions", ucaId],
		queryFn: () => getHighLevelSolutionsByUCA(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Edit High Level Solutions

	const handleEditSolutions = (classNumber: number, classId?: string) => {
		return {
			classNumber,
			classId
		};
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle High Level Solutions Content By Class

	const getSolutionByClassId = (classId: string | undefined) => {
		if (!solutions || solutions.length === 0 || !classId) return null;
		return solutions.find(solution => solution.formalScenarioClassId === classId);
	};
	const renderClassContent = (classNumber: number, classId: string | undefined) => {
		const solution = getSolutionByClassId(classId);

		return (
			<div className={styles.solutions_class_content}>
				<div className={styles.solution}>
					<span className={styles.solution_title}>Process Behavior:</span>
					<span className={styles.solution_content}>
						{solution?.processBehavior || "-"}
					</span>
				</div>
				<div className={styles.solution}>
					<span className={styles.solution_title}>Controller Behavior:</span>
					<span className={styles.solution_content}>
						{solution?.controllerBehavior || "-"}
					</span>
				</div>
				<div className={styles.solution}>
					<span className={styles.solution_title}>Other Solutions:</span>
					<span className={styles.solution_content}>
						{solution?.otherSolutions || "-"}
					</span>
				</div>
				<div className={styles.actions}>
					<Button
						icon={
							classNumber === 1
								? TbCircleDashedNumber1
								: classNumber === 2
								? TbCircleDashedNumber2
								: classNumber === 3
								? TbCircleDashedNumber3
								: TbCircleDashedNumber4
						}
						iconPosition="left"
						variant="dark"
						onClick={() => handleEditSolutions(classNumber, classId || "")}
						size="small"
					>
						Edit High-Level Solutions
					</Button>
				</div>
			</div>
		);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<Container
			title="4.2 Identify High-Level Solutions"
			justTitle
			collapsible
			isLoading={isLoading}
			isError={formalScenarios === undefined || isError || solutions === undefined}
		>
			<ContainerClassLayout
				class1Content={renderClassContent(1, formalScenarios?.class1?.id)}
				class2Content={renderClassContent(2, formalScenarios?.class2?.id)}
				class3Content={renderClassContent(3, formalScenarios?.class3?.id)}
				class4Content={renderClassContent(4, formalScenarios?.class4?.id)}
			/>
		</Container>
	);
}

export default HighLevelSolutionsContainer;
