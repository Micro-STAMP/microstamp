import Button from "@components/Button";
import DualButton from "@components/Button/DualButton";
import { ListItem as Solution } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalRefinedSolutions } from "@components/Modal/ModalEntity";
import NoResultsMessage from "@components/NoResultsMessage";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { IRefinedScenarioReadDto } from "@interfaces/IStep4New/IRefinedScenarios";
import { IRefinedSolutionReadDto } from "@interfaces/IStep4New/IRefinedSolutions";
import { useRefinedSolutions } from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/hooks";
import { useState } from "react";
import { BiPlusCircle } from "react-icons/bi";
import styles from "./RefinedSolutionContent.module.css";

interface IRefinedSolutionContentProps {
	uca: IUnsafeControlActionReadDto;
	refinedSolutionsByClass: IRefinedSolutionReadDto[];
	refinedScenariosByClass: IRefinedScenarioReadDto[];
}
function RefinedSolutionContent({
	uca,
	refinedSolutionsByClass,
	refinedScenariosByClass
}: IRefinedSolutionContentProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Modals

	// Create
	const [modalCreateRefinedSolutionOpen, setModalCreateRefinedSolutionOpen] = useState(false);
	const toggleModalCreateRefinedSolution = () =>
		setModalCreateRefinedSolutionOpen(!modalCreateRefinedSolutionOpen);

	// Update
	const [modalUpdateRefinedSolutionOpen, setModalUpdateRefinedSolutionOpen] = useState(false);
	const toggleModalUpdateRefinedSolution = () =>
		setModalUpdateRefinedSolutionOpen(!modalUpdateRefinedSolutionOpen);

	// Delete
	const [modalDeleteRefinedSolutionOpen, setModalDeleteRefinedSolutionOpen] = useState(false);
	const toggleModalDeleteRefinedSolution = () =>
		setModalDeleteRefinedSolutionOpen(!modalDeleteRefinedSolutionOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Selected Refined Solution

	const [selectedRefinedSolution, setSelectedRefinedSolution] =
		useState<IRefinedSolutionReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Refined Solution Actions

	const {
		onCreateRefinedSolution,
		onDeleteRefinedSolution,
		onUpdateRefinedSolution,
		isCreating,
		isUpdating,
		isDeleting
	} = useRefinedSolutions({
		ucaId: uca.id,
		onUpdateSuccess: () => {
			toggleModalUpdateRefinedSolution();
			setSelectedRefinedSolution(null);
		},
		onDeleteSuccess: () => {
			toggleModalDeleteRefinedSolution();
			setSelectedRefinedSolution(null);
		}
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	// * Handle Active Scenarios

	const usedScenarioIds = new Set(refinedSolutionsByClass.map(sol => sol.refinedScenarioId));
	const scenariosForCreate = refinedScenariosByClass.filter(
		scenario => !usedScenarioIds.has(scenario.id)
	);
	const scenarioForUpdate = selectedRefinedSolution
		? refinedScenariosByClass.filter(
				scenario => scenario.id === selectedRefinedSolution.refinedScenarioId
		  )
		: [];

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<div className={styles.refined_solutions_content}>
				{refinedSolutionsByClass.length > 0 ? (
					<>
						<span className={styles.title}>Refined Solutions:</span>
						<div className={styles.refined_solutions_list}>
							<ListWrapper>
								{refinedSolutionsByClass.map((solution, index) => (
									<Solution.Root key={solution.id}>
										<Solution.Name
											code={"RSO-" + (index + 1)}
											name={solution.mitigation}
											dependencies={[
												"RSC-" +
													(refinedScenariosByClass
														.map(s => s.id)
														.indexOf(solution.refinedScenarioId) +
														1)
											]}
										/>
										<Solution.Actions>
											<DualButton
												onEdit={() => {
													setSelectedRefinedSolution(solution);
													toggleModalUpdateRefinedSolution();
												}}
												onDelete={() => {
													setSelectedRefinedSolution(solution);
													toggleModalDeleteRefinedSolution();
												}}
											/>
										</Solution.Actions>
									</Solution.Root>
								))}
							</ListWrapper>
						</div>
					</>
				) : (
					<NoResultsMessage message="No refined solutions found." />
				)}
				<div className={styles.actions}>
					<Button
						icon={BiPlusCircle}
						iconPosition="left"
						variant="dark"
						onClick={() => {
							toggleModalCreateRefinedSolution();
						}}
						size="small"
					>
						Create Refined Solution
					</Button>
				</div>
			</div>
			<ModalRefinedSolutions
				open={modalCreateRefinedSolutionOpen}
				onClose={toggleModalCreateRefinedSolution}
				onSubmit={onCreateRefinedSolution}
				isLoading={isCreating}
				scenarios={scenariosForCreate}
			/>
			{selectedRefinedSolution && (
				<>
					<ModalRefinedSolutions
						open={modalUpdateRefinedSolutionOpen}
						onClose={() => {
							toggleModalUpdateRefinedSolution();
							setSelectedRefinedSolution(null);
						}}
						onSubmit={data => onUpdateRefinedSolution(selectedRefinedSolution.id, data)}
						refinedSolution={selectedRefinedSolution}
						isLoading={isUpdating}
						scenarios={scenarioForUpdate}
					/>
					<ModalConfirm
						open={modalDeleteRefinedSolutionOpen}
						onClose={() => {
							toggleModalDeleteRefinedSolution();
							setSelectedRefinedSolution(null);
						}}
						onConfirm={() => onDeleteRefinedSolution(selectedRefinedSolution.id)}
						isLoading={isDeleting}
						message="Do you want to delete this refined solution?"
						title="Delete Refined Solution"
						btnText="Delete"
					/>
				</>
			)}
		</>
	);
}

export default RefinedSolutionContent;
