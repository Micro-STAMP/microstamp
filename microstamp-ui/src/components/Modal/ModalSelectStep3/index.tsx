import Button from "@components/Button";
import { SelectSearch } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import {
	ModalSelectComponent,
	ModalSelectControlAction
} from "@components/Modal/ModalSelectOptions";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { getComponents } from "@http/Step2/Components";
import { getControlActions } from "@http/Step2/Interactions/ControlActions";
import { componentsToSelectOptions } from "@interfaces/IStep2/IComponent";
import { controlActionsToSelectOptions } from "@interfaces/IStep2/IInteraction/IControlAction/IControlActionSelectOption";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import styles from "./ModalSelectStep3.module.css";

interface ModalSelectStep3Props extends ModalProps {
	analysisId: string;
	isUpdate?: boolean;
}
function ModalSelectStep3({ open, onClose, analysisId, isUpdate = false }: ModalSelectStep3Props) {
	const navigate = useNavigate();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Component Options

	// Get Components
	const [componentsOptions, setComponentsOptions] = useState<SelectOption[]>([]);
	const { data: components, isLoading: isLoadingComponents } = useQuery({
		queryKey: ["components-select-options", analysisId],
		queryFn: () => getComponents(analysisId)
	});
	useEffect(() => {
		if (components) {
			setComponentsOptions(componentsToSelectOptions(components));
		}
	}, [components]);

	// Selected Component
	const [selectedComponent, setSelectedComponent] = useState<SelectOption | null>(null);
	const [modalSelectComponentsOpen, setModalSelectComponentsOpen] = useState(false);
	const toggleModalSelectComponents = () =>
		setModalSelectComponentsOpen(!modalSelectComponentsOpen);

	useEffect(() => {
		if (componentsOptions.length > 0) {
			setSelectedComponent(componentsOptions[0]);
		}
	}, [componentsOptions]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Control Action Options

	// Get Control Actions
	const [controlActionsOptions, setControlActionsOptions] = useState<SelectOption[]>([]);
	const { data: controlActions, isLoading: isLoadingControlActions } = useQuery({
		queryKey: ["control-actions-select-options", analysisId],
		queryFn: () => getControlActions(analysisId)
	});

	useEffect(() => {
		if (controlActions && selectedComponent) {
			const filteredControlActions = controlActions.filter(
				action => action.connection.source.id === selectedComponent.value
			);
			setControlActionsOptions(controlActionsToSelectOptions(filteredControlActions));
		} else {
			setControlActionsOptions([]);
		}
	}, [controlActions, selectedComponent]);

	// Selected Control Action
	const [selectedCA, setSelectedCA] = useState<SelectOption | null>(null);
	const [modalSelectCAOpen, setModalSelectCAOpen] = useState(false);
	const toggleModalSelectCA = () => setModalSelectCAOpen(!modalSelectCAOpen);

	useEffect(() => {
		if (controlActionsOptions.length > 0) {
			setSelectedCA(controlActionsOptions[0]);
		} else {
			setSelectedCA(null);
		}
	}, [controlActionsOptions]);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	const handleProceed = () => {
		if (selectedComponent && selectedCA) {
			navigate(
				`/analyses/${analysisId}/control-action/${selectedCA.value}/unsafe-control-actions`,
				{ replace: isUpdate }
			);
			onClose();
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<ModalContainer open={open} size="big">
				<ModalHeader
					title={
						isUpdate
							? "Change Control Action Under Analysis"
							: "Control Action for Step 3 Analysis"
					}
					onClose={onClose}
				/>
				<div className={styles.modal_select_step3}>
					<div className={styles.explanation}>
						{isUpdate ? (
							<p>
								Select another <strong>Control Action</strong> to analyze. This will
								update the Unsafe Control Actions being displayed for the selected
								Control Action.
							</p>
						) : (
							<>
								<p>
									To <strong>Identify Unsafe Control Actions</strong>, you need to
									select a specific <strong>Control Action</strong> to analyze.
								</p>
								<p>
									Our approach is based on generating{" "}
									<strong>context tables</strong> (LEVESON, 2018; THOMAS, 2013)
									for each control action, which display all possible contexts
									resulting from combinations of controller variable states. This
									associates each control action with all possible contexts where
									it can be applied. Additionally, you can define custom{" "}
									<strong>rules</strong> (THOMAS, 2013) to automatically identify
									UCAs in specific contexts.
								</p>
							</>
						)}
					</div>
					<div className={styles.options_container}>
						<span className={styles.label}>
							{isUpdate
								? "Select the new Control Action to analyze:"
								: "Select the Control Action you want to analyze:"}
						</span>
						<ModalInputs column="single">
							<SelectSearch
								label="Source Component"
								value={selectedComponent}
								onSearch={toggleModalSelectComponents}
								disabled={componentsOptions.length === 0 || isLoadingComponents}
							/>
							{componentsOptions.length === 0 && !isLoadingComponents && (
								<div className={styles.warning_message}>
									No components available.
								</div>
							)}
							<SelectSearch
								label="Control Action"
								value={selectedCA}
								onSearch={toggleModalSelectCA}
								disabled={
									!selectedComponent ||
									controlActionsOptions.length === 0 ||
									isLoadingControlActions
								}
							/>
							{controlActionsOptions.length === 0 && selectedComponent && (
								<div className={styles.warning_message}>
									No control actions available for the selected component.
								</div>
							)}
						</ModalInputs>
					</div>
				</div>
				<ModalButtons>
					<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
						Cancel
					</Button>
					<Button
						size="small"
						icon={CheckIcon}
						onClick={handleProceed}
						disabled={!selectedComponent || !selectedCA}
					>
						{isUpdate ? "Change Control Action" : "Continue"}
					</Button>
				</ModalButtons>
			</ModalContainer>
			<ModalSelectComponent
				open={modalSelectComponentsOpen}
				onClose={toggleModalSelectComponents}
				analysisId={analysisId}
				components={selectedComponent ? [selectedComponent] : []}
				onChange={(components: SelectOption[]) => setSelectedComponent(components[0])}
				multiple={false}
			/>
			<ModalSelectControlAction
				open={modalSelectCAOpen}
				onClose={toggleModalSelectCA}
				controlActions={selectedCA ? [selectedCA] : []}
				onChange={(controlActions: SelectOption[]) => setSelectedCA(controlActions[0])}
				multiple={false}
				options={controlActionsOptions}
			/>
		</>
	);
}

export default ModalSelectStep3;
