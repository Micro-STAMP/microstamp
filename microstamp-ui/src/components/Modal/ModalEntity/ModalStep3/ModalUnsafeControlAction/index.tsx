import Button from "@components/Button";
import { TypeSelect } from "@components/FormField";
import { hazardsToSelectOptions } from "@components/FormField/MultiSelect/HazardsMultiSelect/util";
import HazardSelect from "@components/FormField/Select/HazardSelect";
import StateSelect from "@components/FormField/Select/StateSelect";
import {
	statesToSelectOptions,
	stateToSelectOption
} from "@components/FormField/Select/StateSelect/util";
import { SelectOption } from "@components/FormField/Templates";
import Loader from "@components/Loader";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { getHazards } from "@http/Step1/Hazards";
import { IControlAction, IVariableReadDto } from "@interfaces/IStep2";
import { IContext, IUnsafeControlActionFormData } from "@interfaces/IStep3";
import { IUCAType, ucaTypeToSelectOption } from "@interfaces/IStep3/IUnsafeControlAction/Enums";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalUnsafeControlAction.module.css";

interface ModalUnsafeControlActionProps extends ModalProps {
	onSubmit: (uca: IUnsafeControlActionFormData) => Promise<void>;
	analysisId: string;
	isLoading?: boolean;
	controlAction: IControlAction;
	context: IContext;
	type: IUCAType;
}
function ModalUnsafeControlAction({
	analysisId,
	controlAction,
	context,
	open,
	onClose,
	onSubmit,
	type,
	isLoading = false
}: ModalUnsafeControlActionProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Hazard Options

	const [hazardsOptions, setHazardsOptions] = useState<SelectOption[]>([]);
	const {
		data: hazards,
		isLoading: isLoadingHazards,
		isError
	} = useQuery({
		queryKey: ["hazards-select-options", analysisId],
		queryFn: () => getHazards(analysisId)
	});

	useEffect(() => {
		if (hazards) {
			setHazardsOptions(hazardsToSelectOptions(hazards));
		}
	}, [hazards]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Variables & States Options

	const variables: IVariableReadDto[] = controlAction.connection.source.variables.concat(
		controlAction.connection.target.variables
	);

	const [selectedStates, setSelectedStates] = useState<Record<string, SelectOption | null>>({});
	const handleStateChange = (variableId: string, state: SelectOption | null) => {
		setSelectedStates(prev => ({
			...prev,
			[variableId]: state
		}));
	};
	useEffect(() => {
		const stateList = Object.values(selectedStates).filter(
			(state): state is SelectOption => !!state
		);
		setUCAData(prev => ({
			...prev,
			states: stateList
		}));
	}, [selectedStates]);

	useEffect(() => {
		context.states.forEach(st => {
			handleStateChange(st.variable.id, stateToSelectOption(st));
		});
	}, [context]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Unsafe Control Action Data

	const [ucaData, setUCAData] = useState<IUnsafeControlActionFormData>({
		states: [],
		type: ucaTypeToSelectOption(type),
		hazard: hazardsOptions[0]
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Unsafe Control Action

	const handleSubmitUnsafeControlAction = async () => {
		if (ucaData.states.length === 0 || !ucaData.type || !ucaData.hazard) {
			toast.warning("A required field is empty.");
			return;
		}

		await onSubmit(ucaData);
		setUCAData({
			states: [],
			type: ucaTypeToSelectOption(type),
			hazard: hazardsOptions[0]
		});
		setSelectedStates({});
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingHazards) return <Loader />;
	if (isError || hazards === undefined) return <h1>Error</h1>;
	return (
		<ModalContainer open={open} size="big">
			<ModalHeader onClose={onClose} title="New Unsafe Control Action" />
			<div className={styles.modal_uca_inputs}>
				<ModalInputs column="double">
					{variables.map(variable => (
						<StateSelect
							key={variable.id}
							label={`${variable.name} State`}
							states={statesToSelectOptions(variable.states)}
							value={selectedStates[variable.id] || null}
							onChange={newValue => handleStateChange(variable.id, newValue)}
							optionsPosition="bottom"
							disabled
						/>
					))}
				</ModalInputs>
				<ModalInputs>
					<TypeSelect
						onChange={(type: SelectOption | null) =>
							setUCAData({ ...ucaData, type: type })
						}
						type={ucaData.type}
						disabled
					/>
				</ModalInputs>
				<ModalInputs>
					<HazardSelect
						value={ucaData.hazard}
						onChange={(value: SelectOption | null) =>
							setUCAData({ ...ucaData, hazard: value })
						}
						hazards={hazardsOptions}
						required
					/>
				</ModalInputs>
			</div>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitUnsafeControlAction}
					isLoading={isLoading}
					size="small"
					icon={CheckIcon}
				>
					Create
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalUnsafeControlAction;
