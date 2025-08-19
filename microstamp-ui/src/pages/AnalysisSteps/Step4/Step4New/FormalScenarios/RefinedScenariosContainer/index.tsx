import Button from "@components/Button";
import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as Scenario } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalRefinedScenarios } from "@components/Modal/ModalEntity";
import NoResultsMessage from "@components/NoResultsMessage";
import {
	createRefinedScenario,
	deleteRefinedScenario,
	getRefinedScenariosByUCA,
	updateRefinedScenario
} from "@http/Step4New/RefinedScenarios";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import {
	IFormalScenarioClassDto,
	IFormalScenariosReadDto
} from "@interfaces/IStep4New/IFormalScenarios";
import {
	groupRefinedScenariosByClass,
	IRefinedScenarioFormData,
	IRefinedScenarioInsertDto,
	IRefinedScenarioReadDto,
	IRefinedScenarioUpdateDto
} from "@interfaces/IStep4New/IRefinedScenarios";
import ContainerClassLayout from "@pages/AnalysisSteps/Step4/Step4New/components/ContainerClassLayout";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiPlusCircle } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./RefinedScenariosContainer.module.css";

interface RefinedScenariosContainerProps {
	uca: IUnsafeControlActionReadDto;
	formalScenarios: IFormalScenariosReadDto;
}

function RefinedScenariosContainer({ uca, formalScenarios }: RefinedScenariosContainerProps) {
	const queryClient = useQueryClient();

	const [selectedClass, setSelectedClass] = useState<IFormalScenarioClassDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Refined Scenario

	const [modalCreateRefinedScenarioOpen, setModalCreateRefinedScenarioOpen] = useState(false);
	const toggleModalCreateRefinedScenario = () =>
		setModalCreateRefinedScenarioOpen(!modalCreateRefinedScenarioOpen);

	const { mutateAsync: requestCreateRefinedScenario, isPending: isCreating } = useMutation({
		mutationFn: (scenario: IRefinedScenarioInsertDto) => createRefinedScenario(scenario),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["refined-scenarios"] });
			toast.success("Refined scenario created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateRefinedScenario = async (scenarioData: IRefinedScenarioFormData) => {
		const scenario: IRefinedScenarioInsertDto = {
			commonCauseId: scenarioData.commonCauseId,
			formalScenarioClassId: scenarioData.formalScenarioClassId,
			refinedScenario: scenarioData.refinedScenario,
			unsafeControlActionId: uca.id
		};
		await requestCreateRefinedScenario(scenario);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Refined Scenario

	const [selectedRefinedScenario, setSelectedRefinedScenario] =
		useState<IRefinedScenarioReadDto | null>(null);

	const [modalUpdateRefinedScenarioOpen, setModalUpdateRefinedScenarioOpen] = useState(false);
	const toggleModalUpdateRefinedScenario = () =>
		setModalUpdateRefinedScenarioOpen(!modalUpdateRefinedScenarioOpen);

	const { mutateAsync: requestUpdateRefinedScenario, isPending: isUpdating } = useMutation({
		mutationFn: ({ scenario, id }: { scenario: IRefinedScenarioUpdateDto; id: string }) =>
			updateRefinedScenario(id, scenario),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["refined-scenarios"] });
			toast.success("Refined scenario updated.");
			setSelectedRefinedScenario(null);
			toggleModalUpdateRefinedScenario();
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateRefinedScenario = async (
		id: string,
		scenarioData: IRefinedScenarioFormData
	) => {
		const scenario: IRefinedScenarioUpdateDto = {
			commonCauseId: scenarioData.commonCauseId,
			formalScenarioClassId: scenarioData.formalScenarioClassId,
			refinedScenario: scenarioData.refinedScenario
		};
		await requestUpdateRefinedScenario({ id, scenario });
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Refined Scenario

	const [modalDeleteRefinedScenarioOpen, setModalDeleteRefinedScenarioOpen] = useState(false);
	const toggleModalDeleteRefinedScenario = () =>
		setModalDeleteRefinedScenarioOpen(!modalDeleteRefinedScenarioOpen);

	const { mutateAsync: requestDeleteRefinedScenario, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteRefinedScenario(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["refined-scenarios"] });
			toast.success("Refined scenario deleted.");
			setSelectedRefinedScenario(null);
			toggleModalDeleteRefinedScenario();
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteRefinedScenario = async (id: string) => {
		await requestDeleteRefinedScenario(id);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Refined Scenarios

	const {
		data: refinedScenarios,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["refined-scenarios", uca.id],
		queryFn: () => getRefinedScenariosByUCA(uca.id)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Group Scenarios & Class Content

	const groupedScenarios = groupRefinedScenariosByClass(refinedScenarios, formalScenarios);

	const renderClassContent = (
		scenarios: IRefinedScenarioReadDto[],
		classData: IFormalScenarioClassDto
	) => {
		return (
			<div className={styles.refined_scenarios_class_content}>
				{scenarios.length > 0 ? (
					<>
						<span className={styles.title}>Refined Scenarios:</span>
						<div className={styles.refined_scenarios_list}>
							<ListWrapper>
								{scenarios.map((scenario, index) => (
									<Scenario.Root key={scenario.id}>
										<Scenario.Name
											code={"RS-" + (index + 1)}
											name={scenario.refinedScenario}
										/>
										<Scenario.Actions>
											<DualButton
												onEdit={() => {
													setSelectedClass(classData);
													setSelectedRefinedScenario(scenario);
													toggleModalUpdateRefinedScenario();
												}}
												onDelete={() => {
													setSelectedClass(classData);
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
							setSelectedClass(classData);
							toggleModalCreateRefinedScenario();
						}}
						size="small"
					>
						Create Refined Scenario
					</Button>
				</div>
			</div>
		);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<Container
				title="4.3 Identify Refined Scenarios"
				justTitle
				collapsible
				isLoading={isLoading}
				isError={isError || refinedScenarios === undefined}
			>
				<ContainerClassLayout
					class1Content={renderClassContent(
						groupedScenarios.class1,
						formalScenarios.class1
					)}
					class2Content={renderClassContent(
						groupedScenarios.class2,
						formalScenarios.class2
					)}
					class3Content={renderClassContent(
						groupedScenarios.class3,
						formalScenarios.class3
					)}
					class4Content={renderClassContent(
						groupedScenarios.class4,
						formalScenarios.class4
					)}
				/>
			</Container>
			{selectedClass && (
				<>
					<ModalRefinedScenarios
						uca={uca}
						onSubmit={handleCreateRefinedScenario}
						isLoading={isCreating}
						open={modalCreateRefinedScenarioOpen}
						onClose={toggleModalCreateRefinedScenario}
						formalScenarioClassId={selectedClass.id}
					/>
					{selectedRefinedScenario && (
						<>
							<ModalRefinedScenarios
								uca={uca}
								onSubmit={data =>
									handleUpdateRefinedScenario(selectedRefinedScenario.id, data)
								}
								refinedScenario={selectedRefinedScenario}
								isLoading={isUpdating}
								open={modalUpdateRefinedScenarioOpen}
								onClose={() => {
									toggleModalUpdateRefinedScenario();
									setSelectedRefinedScenario(null);
								}}
								formalScenarioClassId={selectedClass.id}
							/>
							<ModalConfirm
								open={modalDeleteRefinedScenarioOpen}
								onClose={() => {
									toggleModalDeleteRefinedScenario();
									setSelectedRefinedScenario(null);
								}}
								onConfirm={() =>
									handleDeleteRefinedScenario(selectedRefinedScenario.id)
								}
								isLoading={isDeleting}
								message="Do you want to delete this refined scenario?"
								title="Delete Refined Scenario"
								btnText="Delete"
							/>
						</>
					)}
				</>
			)}
		</>
	);
}

export default RefinedScenariosContainer;
