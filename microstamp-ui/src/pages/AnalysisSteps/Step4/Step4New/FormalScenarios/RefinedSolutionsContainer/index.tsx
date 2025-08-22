import Button from "@components/Button";
import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as Solution } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalRefinedSolutions } from "@components/Modal/ModalEntity/ModalStep4New";
import NoResultsMessage from "@components/NoResultsMessage";
import {
	createRefinedSolution,
	deleteRefinedSolution,
	getRefinedSolutionsByUCA,
	updateRefinedSolution
} from "@http/Step4New/RefinedSolutions";
import { IRefinedScenarioByClass } from "@interfaces/IStep4New/IRefinedScenarios";
import {
	groupRefinedSolutionsByClass,
	IRefinedSolutionFormData,
	IRefinedSolutionInsertDto,
	IRefinedSolutionReadDto,
	IRefinedSolutionUpdateDto
} from "@interfaces/IStep4New/IRefinedSolutions";
import ContainerClassLayout from "@pages/AnalysisSteps/Step4/Step4New/components/ContainerClassLayout";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiPlusCircle } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./RefinedSolutionsContainer.module.css";

interface RefinedSolutionsContainerProps {
	ucaId: string;
	refinedScenarios: IRefinedScenarioByClass;
}
function RefinedSolutionsContainer({ ucaId, refinedScenarios }: RefinedSolutionsContainerProps) {
	const queryClient = useQueryClient();

	const [activeClass, setActiveClass] = useState<keyof typeof refinedScenarios | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Refined Solution

	const [modalCreateRefinedSolutionOpen, setModalCreateRefinedSolutionOpen] = useState(false);
	const toggleModalCreateRefinedSolution = () =>
		setModalCreateRefinedSolutionOpen(!modalCreateRefinedSolutionOpen);

	const { mutateAsync: requestCreateRefinedSolution, isPending: isCreating } = useMutation({
		mutationFn: (solution: IRefinedSolutionInsertDto) => createRefinedSolution(solution),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["refined-solutions"] });
			toast.success("Refined solution created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateRefinedSolution = async (solutionData: IRefinedSolutionFormData) => {
		const solution: IRefinedSolutionInsertDto = {
			mitigation: solutionData.mitigation,
			refinedScenarioId: solutionData.refinedScenario!.value
		};
		await requestCreateRefinedSolution(solution);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Refined Solution

	const [selectedRefinedSolution, setSelectedRefinedSolution] =
		useState<IRefinedSolutionReadDto | null>(null);

	const [modalUpdateRefinedSolutionOpen, setModalUpdateRefinedSolutionOpen] = useState(false);
	const toggleModalUpdateRefinedSolution = () =>
		setModalUpdateRefinedSolutionOpen(!modalUpdateRefinedSolutionOpen);

	const { mutateAsync: requestUpdateRefinedSolution, isPending: isUpdating } = useMutation({
		mutationFn: ({ solution, id }: { solution: IRefinedSolutionUpdateDto; id: string }) =>
			updateRefinedSolution(solution, id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["refined-solutions"] });
			toast.success("Refined solution updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateRefinedSolution = async (
		id: string,
		solutionData: IRefinedSolutionFormData
	) => {
		const solution: IRefinedSolutionUpdateDto = {
			mitigation: solutionData.mitigation
		};
		await requestUpdateRefinedSolution({ id, solution });
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Refined Solution

	const [modalDeleteRefinedSolutionOpen, setModalDeleteRefinedSolutionOpen] = useState(false);
	const toggleModalDeleteRefinedSolution = () =>
		setModalDeleteRefinedSolutionOpen(!modalDeleteRefinedSolutionOpen);

	const { mutateAsync: requestDeleteRefinedSolution, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteRefinedSolution(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["refined-solutions"] });
			toast.success("Refined solution deleted.");
			setSelectedRefinedSolution(null);
			toggleModalDeleteRefinedSolution();
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteRefinedSolution = async (id: string) => {
		await requestDeleteRefinedSolution(id);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Refined Solutions

	const {
		data: refinedSolutions,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["refined-solutions", ucaId],
		queryFn: () => getRefinedSolutionsByUCA(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Group Solutions & Class Content

	const groupedSolutions = groupRefinedSolutionsByClass(refinedSolutions, refinedScenarios);

	const renderClassContent = (
		classKey: keyof typeof refinedScenarios,
		solutions: IRefinedSolutionReadDto[]
	) => {
		return (
			<div className={styles.refined_solutions_class_content}>
				{solutions.length > 0 ? (
					<>
						<span className={styles.title}>Refined Solutions:</span>
						<div className={styles.refined_solutions_list}>
							<ListWrapper>
								{solutions.map((solution, index) => (
									<Solution.Root key={solution.id}>
										<Solution.Name
											code={"RSO-" + (index + 1)}
											name={solution.mitigation}
											dependencies={[
												"RSC-" +
													refinedScenarios[classKey]
														.map(s => s.id)
														.indexOf(solution.refinedScenarioId)
											]}
										/>
										<Solution.Actions>
											<DualButton
												onEdit={() => {
													setSelectedRefinedSolution(solution);
													setActiveClass(classKey);
													toggleModalUpdateRefinedSolution();
												}}
												onDelete={() => {
													setSelectedRefinedSolution(solution);
													setActiveClass(classKey);
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
							setActiveClass(classKey);
							toggleModalCreateRefinedSolution();
						}}
						size="small"
					>
						Create Refined Solution
					</Button>
				</div>
			</div>
		);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Active Scenarios

	const scenariosOfActiveClass = activeClass ? refinedScenarios[activeClass] : [];
	const solutionsOfActiveClass = activeClass ? groupedSolutions[activeClass] : [];
	const usedScenarioIds = new Set(solutionsOfActiveClass.map(sol => sol.refinedScenarioId));
	const availableScenariosForCreate = scenariosOfActiveClass.filter(
		scenario => !usedScenarioIds.has(scenario.id)
	);
	const scenarioForUpdate =
		selectedRefinedSolution && activeClass
			? scenariosOfActiveClass.filter(
					scenario => scenario.id === selectedRefinedSolution.refinedScenarioId
			  )
			: [];

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<Container
				title="4.4 Identify Refined Solutions"
				justTitle
				collapsible
				isLoading={isLoading}
				isError={isError || refinedSolutions === undefined}
			>
				<ContainerClassLayout
					class1Content={renderClassContent("class1", groupedSolutions.class1)}
					class2Content={renderClassContent("class2", groupedSolutions.class2)}
					class3Content={renderClassContent("class3", groupedSolutions.class3)}
					class4Content={renderClassContent("class4", groupedSolutions.class4)}
				/>
			</Container>
			<ModalRefinedSolutions
				open={modalCreateRefinedSolutionOpen}
				onClose={() => {
					toggleModalCreateRefinedSolution();
					setActiveClass(null);
				}}
				scenarios={availableScenariosForCreate}
				onSubmit={handleCreateRefinedSolution}
				isLoading={isCreating}
			/>
			{selectedRefinedSolution && (
				<>
					<ModalRefinedSolutions
						open={modalUpdateRefinedSolutionOpen}
						onClose={() => {
							toggleModalUpdateRefinedSolution();
							setSelectedRefinedSolution(null);
							setActiveClass(null);
						}}
						scenarios={scenarioForUpdate}
						onSubmit={solution =>
							handleUpdateRefinedSolution(selectedRefinedSolution.id, solution)
						}
						refinedSolution={selectedRefinedSolution}
						isLoading={isUpdating}
					/>
					<ModalConfirm
						open={modalDeleteRefinedSolutionOpen}
						onClose={() => {
							toggleModalDeleteRefinedSolution();
							setSelectedRefinedSolution(null);
							setActiveClass(null);
						}}
						onConfirm={() => handleDeleteRefinedSolution(selectedRefinedSolution.id)}
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

export default RefinedSolutionsContainer;
