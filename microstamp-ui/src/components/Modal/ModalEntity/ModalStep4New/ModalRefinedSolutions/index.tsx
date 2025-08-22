import Button from "@components/Button";
import { SelectSearch, Textarea } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import ModalSelectOptions from "@components/Modal/ModalSelectOptions";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import {
	IRefinedScenarioReadDto,
	refinedScenariosToSelectOptions,
	refinedScenarioToSelectOption
} from "@interfaces/IStep4New/IRefinedScenarios";
import {
	IRefinedSolutionFormData,
	IRefinedSolutionReadDto
} from "@interfaces/IStep4New/IRefinedSolutions";
import { useState } from "react";
import { BiCheckCircle, BiUndo } from "react-icons/bi";
import { toast } from "sonner";

interface ModalRefinedSolutionsProps extends ModalProps {
	scenarios: IRefinedScenarioReadDto[];
	onSubmit: (refinedSolution: IRefinedSolutionFormData) => Promise<void>;
	isLoading?: boolean;
	refinedSolution?: IRefinedSolutionReadDto;
}
function ModalRefinedSolutions({
	open,
	onClose,
	scenarios,
	onSubmit,
	isLoading,
	refinedSolution
}: ModalRefinedSolutionsProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Refined Solution Data

	const [refinedSolutionData, setRefinedSolutionData] = useState<IRefinedSolutionFormData>({
		refinedScenario: refinedSolution
			? refinedScenarioToSelectOption(
					scenarios.find(s => s.id === refinedSolution.refinedScenarioId)!
			  )
			: null,
		mitigation: refinedSolution ? refinedSolution.mitigation : ""
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Search Refined Scenarios

	const [modalSearchRefinedScenariosOpen, setModalSearchRefinedScenariosOpen] = useState(false);
	const toggleModalSearchRefinedScenarios = () =>
		setModalSearchRefinedScenariosOpen(!modalSearchRefinedScenariosOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Search Refined Scenarios

	const handleSubmitRefinedSolution = async () => {
		if (!refinedSolutionData.refinedScenario || !refinedSolutionData.mitigation) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(refinedSolutionData);
		if (!refinedSolution) {
			setRefinedSolutionData({
				mitigation: "",
				refinedScenario: null
			});
		}
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<ModalContainer open={open} size="big">
				<ModalHeader
					onClose={onClose}
					title={refinedSolution ? "Update Refined Solution" : "Create Refined Solution"}
				/>

				<ModalInputs column="single">
					<SelectSearch
						value={refinedSolutionData.refinedScenario}
						onSearch={toggleModalSearchRefinedScenarios}
						disabled={refinedSolution !== undefined}
						label={refinedSolution ? "Refined Scenario" : "Select Refined Scenario"}
					/>
					<Textarea
						label="Mitigation"
						value={refinedSolutionData.mitigation}
						onChange={(value: string) =>
							setRefinedSolutionData({
								...refinedSolutionData,
								mitigation: value
							})
						}
						rows={6}
						required
					/>
				</ModalInputs>
				<ModalButtons>
					<Button variant="dark" onClick={onClose} size="small" icon={BiUndo}>
						Cancel
					</Button>
					<Button
						onClick={handleSubmitRefinedSolution}
						isLoading={isLoading}
						size="small"
						icon={BiCheckCircle}
					>
						{refinedSolution ? "Update Refined Solution" : "Create Refined Solution"}
					</Button>
				</ModalButtons>
			</ModalContainer>
			{!refinedSolution && (
				<ModalSelectOptions
					open={modalSearchRefinedScenariosOpen}
					onClose={toggleModalSearchRefinedScenarios}
					title={"Select the Refined Scenario"}
					onChange={(value: SelectOption[]) =>
						setRefinedSolutionData({
							...refinedSolutionData,
							refinedScenario: value[0]
						})
					}
					selectedOptions={
						refinedSolutionData.refinedScenario
							? [refinedSolutionData.refinedScenario]
							: []
					}
					options={refinedScenariosToSelectOptions(scenarios)}
				/>
			)}
		</>
	);
}

export default ModalRefinedSolutions;
