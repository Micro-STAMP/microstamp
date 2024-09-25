import Button from "@components/Button";
import ComponentSelect from "@components/FormField/Select/ComponentSelect";
import { componentsToSelectOptions } from "@components/FormField/Select/ComponentSelect/util";
import ControlActionSelect from "@components/FormField/Select/ControlActionSelect";
import { controlActionsToSelectOptions } from "@components/FormField/Select/ControlActionSelect/util";
import { SelectOption } from "@components/FormField/Templates";
import Loader from "@components/Loader";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { getComponents } from "@http/Step2/Components";
import { getControlActions } from "@http/Step2/Interactions/ControlActions";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import styles from "./ModalSelectControlAction.module.css";

interface ModalSelectControlActionProps extends ModalProps {
	analysisId: string;
}

function ModalSelectControlAction({ open, onClose, analysisId }: ModalSelectControlActionProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Component Options

	const [componentsOptions, setComponentsOptions] = useState<SelectOption[]>([]);
	const {
		data: components,
		isLoading: isLoadingComponents,
		isError: isErrorComponents
	} = useQuery({
		queryKey: ["control-action-components-select-options", analysisId],
		queryFn: () => getComponents(analysisId)
	});

	useEffect(() => {
		if (components) {
			setComponentsOptions(componentsToSelectOptions(components));
		}
	}, [components]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Selected Component

	const [selectedComponent, setSelectedComponent] = useState<SelectOption | null>(
		componentsOptions[0]
	);

	useEffect(() => {
		if (componentsOptions.length > 0) {
			setSelectedComponent(componentsOptions[0]);
		}
	}, [componentsOptions]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Control Action Options

	const [controlActionsOptions, setControlActionsOptions] = useState<SelectOption[]>([]);
	const {
		data: controlActions,
		isLoading: isLoadingControlActions,
		isError: isErrorControlActions
	} = useQuery({
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

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Selected Control Action

	const [selectedCA, setSelectedCA] = useState<SelectOption | null>(null);

	useEffect(() => {
		if (controlActionsOptions.length > 0) {
			setSelectedCA(controlActionsOptions[0]);
		} else {
			setSelectedCA(null);
		}
	}, [controlActionsOptions]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Control Action

	const navigate = useNavigate();
	const handleSelectControlAction = async () => {
		if (selectedCA) {
			navigate(`control-action/${selectedCA.value}`);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingComponents || isLoadingControlActions) return <Loader />;
	if (isErrorComponents || isErrorControlActions || controlActions === undefined)
		return <h1>Error</h1>;
	return (
		<ModalContainer open={open} size="normal">
			<ModalHeader onClose={onClose} title="Select the Control Action" />
			<div className={styles.control_actions_select}>
				<ModalInputs>
					{componentsOptions.length > 0 ? (
						<ComponentSelect
							label="Component"
							components={componentsOptions}
							onChange={(component: SelectOption | null) =>
								setSelectedComponent(component)
							}
							value={selectedComponent}
							optionsPosition="bottom"
							required
						/>
					) : (
						<div className={styles.no_components_message}>
							There are no components available.
						</div>
					)}
					{componentsOptions.length > 0 && (
						<ControlActionSelect
							controlActions={controlActionsOptions}
							onChange={(ca: SelectOption | null) => setSelectedCA(ca)}
							value={selectedCA}
							disabled={controlActionsOptions.length === 0}
						/>
					)}
				</ModalInputs>
				{controlActionsOptions.length === 0 && selectedComponent && (
					<div className={styles.warning_message}>
						No control actions available for the selected component.
					</div>
				)}
			</div>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSelectControlAction}
					isLoading={isLoadingControlActions}
					size="small"
					icon={CheckIcon}
					disabled={!selectedCA}
				>
					Confirm
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalSelectControlAction;
