import Button from "@components/Button";
import { Textarea } from "@components/FormField";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { updateHighLevelSolutions } from "@http/Step4New/HighLevelSolutions";
import { IFormalScenarioClassDto } from "@interfaces/IStep4New/IFormalScenarios";
import {
	IHighLevelSolutionsFormData,
	IHighLevelSolutionsReadDto,
	IHighLevelSolutionsUpdateDto
} from "@interfaces/IStep4New/IHighLevelSolutions";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiChevronUp, BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalHighLevelSolutions.module.css";

interface ModalHighLevelSolutionsProps extends ModalProps {
	formalScenario?: IFormalScenarioClassDto;
	highLevelSolutions: IHighLevelSolutionsReadDto;
}
function ModalHighLevelSolutions({
	formalScenario,
	highLevelSolutions,
	open,
	onClose
}: ModalHighLevelSolutionsProps) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle High Level Solutions Data

	const [highLevelSolutionsData, setHighLevelSolutionsData] =
		useState<IHighLevelSolutionsFormData>({
			controllerBehavior: highLevelSolutions.controllerBehavior,
			otherSolutions: highLevelSolutions.otherSolutions,
			processBehavior: highLevelSolutions.processBehavior
		});

	useEffect(() => {
		setHighLevelSolutionsData({
			controllerBehavior: highLevelSolutions.controllerBehavior,
			otherSolutions: highLevelSolutions.otherSolutions,
			processBehavior: highLevelSolutions.processBehavior
		});
	}, [highLevelSolutions]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle High Level Solutions Update Request

	const { mutateAsync: requestUpdateHighLevelSolutions, isPending } = useMutation({
		mutationFn: (solutions: IHighLevelSolutionsUpdateDto) =>
			updateHighLevelSolutions(solutions, highLevelSolutions.id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["high-level-solutions"] });
			toast.success("High Level Solutions updated successfully.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Submit

	const handleSubmit = async () => {
		if (
			!highLevelSolutionsData.controllerBehavior ||
			!highLevelSolutionsData.otherSolutions ||
			!highLevelSolutionsData.processBehavior
		) {
			toast.warning("A required field is empty.");
			return;
		}
		const updatedSolutions: IHighLevelSolutionsUpdateDto = {
			controllerBehavior: highLevelSolutionsData.controllerBehavior,
			otherSolutions: highLevelSolutionsData.otherSolutions,
			processBehavior: highLevelSolutionsData.processBehavior
		};
		await requestUpdateHighLevelSolutions(updatedSolutions);
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Get Scenario Class

	const [scenariosOpen, setScenariosOpen] = useState(true);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<ModalContainer open={open} size="large">
			<ModalHeader onClose={onClose} title={"Update High Level Solutions"} />
			{formalScenario && (
				<div className={styles.formal_scenarios}>
					<div
						className={styles.header}
						onClick={() => setScenariosOpen(open => !open)}
						role="button"
					>
						<span className={styles.title}>High Level Scenarios</span>
						<BiChevronUp
							className={`${styles.chevron_icon} ${
								!scenariosOpen ? styles.collapsed : ""
							}`}
						/>
					</div>
					{scenariosOpen && (
						<div className={styles.content}>
							<div className={styles.formal_scenarios_item}>
								<span className={styles.formal_scenarios_label}>[Output]</span>
								<span className={styles.formal_scenarios_value}>
									{formalScenario.output}
								</span>
							</div>
							<div className={styles.formal_scenarios_item}>
								<span className={styles.formal_scenarios_label}>[Input]</span>
								<span className={styles.formal_scenarios_value}>
									{formalScenario.input}
								</span>
							</div>
						</div>
					)}
				</div>
			)}
			<ModalInputs column="triple">
				<Textarea
					label="Controller Behavior"
					value={highLevelSolutionsData.controllerBehavior}
					onChange={(value: string) =>
						setHighLevelSolutionsData({
							...highLevelSolutionsData,
							controllerBehavior: value
						})
					}
					rows={6}
					required
				/>
				<Textarea
					label="Process Behavior"
					value={highLevelSolutionsData.processBehavior}
					onChange={(value: string) =>
						setHighLevelSolutionsData({
							...highLevelSolutionsData,
							processBehavior: value
						})
					}
					rows={6}
					required
				/>
				<Textarea
					label="Other Solutions"
					value={highLevelSolutionsData.otherSolutions}
					onChange={(value: string) =>
						setHighLevelSolutionsData({
							...highLevelSolutionsData,
							otherSolutions: value
						})
					}
					rows={6}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button onClick={handleSubmit} isLoading={isPending} size="small" icon={CheckIcon}>
					Update
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalHighLevelSolutions;
