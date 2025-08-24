import Button from "@components/Button";
import DualButton from "@components/Button/DualButton";
import { ListItem as Scenario } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalRefinedScenarios } from "@components/Modal/ModalEntity";
import NoResultsMessage from "@components/NoResultsMessage";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { IRefinedScenarioReadDto } from "@interfaces/IStep4New/IRefinedScenarios";
import useRefinedScenarios from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/hooks/useRefinedScenarios";
import { useState } from "react";
import { BiPlusCircle } from "react-icons/bi";
import styles from "./RefinedScenarioContent.module.css";

interface IRefinedScenarioContentProps {
	scenarios: IRefinedScenarioReadDto[];
	uca: IUnsafeControlActionReadDto;
	formalScenarioClassId: string;
}
function RefinedScenarioContent({
	scenarios,
	uca,
	formalScenarioClassId
}: IRefinedScenarioContentProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Modals

	// Create
	const [modalCreateRefinedScenarioOpen, setModalCreateRefinedScenarioOpen] = useState(false);
	const toggleModalCreateRefinedScenario = () =>
		setModalCreateRefinedScenarioOpen(!modalCreateRefinedScenarioOpen);

	// Update
	const [modalUpdateRefinedScenarioOpen, setModalUpdateRefinedScenarioOpen] = useState(false);
	const toggleModalUpdateRefinedScenario = () =>
		setModalUpdateRefinedScenarioOpen(!modalUpdateRefinedScenarioOpen);

	// Delete
	const [modalDeleteRefinedScenarioOpen, setModalDeleteRefinedScenarioOpen] = useState(false);
	const toggleModalDeleteRefinedScenario = () =>
		setModalDeleteRefinedScenarioOpen(!modalDeleteRefinedScenarioOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Selected Refined Scenario

	const [selectedRefinedScenario, setSelectedRefinedScenario] =
		useState<IRefinedScenarioReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Refined Scenario Actions

	const {
		onCreateRefinedScenario,
		onDeleteRefinedScenario,
		onUpdateRefinedScenario,
		isCreating,
		isUpdating,
		isDeleting
	} = useRefinedScenarios({
		ucaId: uca.id,
		onUpdateSuccess: () => {
			toggleModalUpdateRefinedScenario();
			setSelectedRefinedScenario(null);
		},
		onDeleteSuccess: () => {
			toggleModalDeleteRefinedScenario();
			setSelectedRefinedScenario(null);
		}
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<div className={styles.refined_scenarios_content}>
				{scenarios.length > 0 ? (
					<>
						<span className={styles.title}>Refined Scenarios:</span>
						<div className={styles.refined_scenarios_list}>
							<ListWrapper>
								{scenarios.map((scenario, index) => (
									<Scenario.Root key={scenario.id}>
										<Scenario.Name
											code={"RSC-" + (index + 1)}
											name={scenario.refinedScenario}
										/>
										<Scenario.Actions>
											<DualButton
												onEdit={() => {
													setSelectedRefinedScenario(scenario);
													toggleModalUpdateRefinedScenario();
												}}
												onDelete={() => {
													setSelectedRefinedScenario(scenario);
													toggleModalDeleteRefinedScenario();
												}}
											/>
										</Scenario.Actions>
									</Scenario.Root>
								))}
							</ListWrapper>
						</div>
					</>
				) : (
					<NoResultsMessage message="No refined scenarios found." />
				)}
				<div className={styles.actions}>
					<Button
						icon={BiPlusCircle}
						iconPosition="left"
						variant="dark"
						onClick={() => {
							toggleModalCreateRefinedScenario();
						}}
						size="small"
					>
						Create Refined Scenario
					</Button>
				</div>
			</div>
			<ModalRefinedScenarios
				uca={uca}
				onSubmit={onCreateRefinedScenario}
				isLoading={isCreating}
				open={modalCreateRefinedScenarioOpen}
				onClose={toggleModalCreateRefinedScenario}
				formalScenarioClassId={formalScenarioClassId}
			/>
			{selectedRefinedScenario && (
				<>
					<ModalRefinedScenarios
						uca={uca}
						onSubmit={data => onUpdateRefinedScenario(selectedRefinedScenario.id, data)}
						refinedScenario={selectedRefinedScenario}
						isLoading={isUpdating}
						open={modalUpdateRefinedScenarioOpen}
						onClose={() => {
							toggleModalUpdateRefinedScenario();
							setSelectedRefinedScenario(null);
						}}
						formalScenarioClassId={formalScenarioClassId}
					/>
					<ModalConfirm
						open={modalDeleteRefinedScenarioOpen}
						onClose={() => {
							toggleModalDeleteRefinedScenario();
							setSelectedRefinedScenario(null);
						}}
						onConfirm={() => onDeleteRefinedScenario(selectedRefinedScenario.id)}
						isLoading={isDeleting}
						message="Do you want to delete this refined scenario?"
						title="Delete Refined Scenario"
						btnText="Delete"
					/>
				</>
			)}
		</>
	);
}

export default RefinedScenarioContent;
