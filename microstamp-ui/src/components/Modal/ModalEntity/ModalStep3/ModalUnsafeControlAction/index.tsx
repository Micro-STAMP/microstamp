import Button from "@components/Button";
import { Checkbox, TypeSelect } from "@components/FormField";
import { hazardsToSelectOptions } from "@components/FormField/MultiSelect/HazardsMultiSelect/util";
import HazardSelect from "@components/FormField/Select/HazardSelect";
import StateSelect from "@components/FormField/Select/StateSelect";
import ContextStates from "@components/FormField/Select/StateSelect/ContextStates";
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
import NoResultsMessage from "@components/NoResultsMessage";
import { getHazards } from "@http/Step1/Hazards";
import { IControlAction, IVariableReadDto } from "@interfaces/IStep2";
import {
	IContext,
	INotUnsafeContextFormData,
	IUnsafeControlActionFormData
} from "@interfaces/IStep3";
import { IUCAType, ucaTypeToSelectOption } from "@interfaces/IStep3/IUnsafeControlAction/Enums";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalUnsafeControlAction.module.css";

interface ModalUnsafeControlActionProps extends ModalProps {
	onSubmitUCA: (uca: IUnsafeControlActionFormData) => Promise<void>;
	onSubmitNotUnsafeContext: (nuc: INotUnsafeContextFormData) => Promise<void>;
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
	onSubmitUCA,
	onSubmitNotUnsafeContext,
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
	const resetForm = () => {
		setUCAData({
			states: [],
			type: ucaTypeToSelectOption(type),
			hazard: hazardsOptions[0]
		});
		setSelectedStates({});
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Unsafe Control Action

	const handleSubmitUnsafeControlAction = async () => {
		if (ucaData.states.length === 0 || !ucaData.type || !ucaData.hazard) {
			toast.warning("A required field is empty.");
			return;
		}

		await onSubmitUCA(ucaData);
		resetForm();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Not Unsafe Context

	const handleSubmitNotUnsafeContext = async () => {
		if (ucaData.states.length === 0 || !ucaData.type) {
			toast.warning("A required field is empty.");
			return;
		}
		const notUnsafeContextData: INotUnsafeContextFormData = {
			states: ucaData.states,
			type: ucaData.type
		};
		await onSubmitNotUnsafeContext(notUnsafeContextData);
		resetForm();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Is Unsafe State and Submit

	const [isUnsafe, setIsUnsafe] = useState(true);

	const handleSubmit = async () => {
		if (isUnsafe) {
			await handleSubmitUnsafeControlAction();
		} else {
			await handleSubmitNotUnsafeContext();
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingHazards)
		return (
			<ModalContainer open={open} size="small">
				<Loader />
			</ModalContainer>
		);
	if (isError || hazards === undefined)
		return (
			<ModalContainer open={open} size="small">
				<ModalHeader onClose={onClose} title="Error" />
				<NoResultsMessage message="Error loading modal." />
			</ModalContainer>
		);
	return (
		<ModalContainer open={open} size="big">
			<ModalHeader
				onClose={onClose}
				title={isUnsafe ? "New Unsafe Control Action" : "New Not Unsafe Context"}
			/>
			<div className={styles.modal_uca_inputs}>
				<ContextStates>
					{variables.map(variable => (
						<StateSelect
							key={variable.id}
							label={variable.name}
							states={statesToSelectOptions(variable.states)}
							value={selectedStates[variable.id] || null}
							onChange={newValue => handleStateChange(variable.id, newValue)}
							optionsPosition="bottom"
							disabled
						/>
					))}
				</ContextStates>
				<ModalInputs>
					<TypeSelect
						onChange={(type: SelectOption | null) =>
							setUCAData({ ...ucaData, type: type })
						}
						type={ucaData.type}
						disabled
					/>
				</ModalInputs>
				<div style={{ width: "100%", margin: "0.8rem 0" }}>
					<Checkbox
						checked={isUnsafe}
						label="Is Unsafe?"
						onChange={checked => setIsUnsafe(checked)}
					/>
				</div>
				{isUnsafe && (
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
				)}
			</div>

			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button onClick={handleSubmit} isLoading={isLoading} size="small" icon={CheckIcon}>
					Create
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalUnsafeControlAction;
