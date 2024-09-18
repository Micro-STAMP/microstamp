import Button from "@components/Button";
import { Input, TypesMultiSelect } from "@components/FormField";
import { hazardsToSelectOptions } from "@components/FormField/MultiSelect/HazardsMultiSelect/util";
import HazardSelect from "@components/FormField/Select/HazardSelect";
import StateSelect from "@components/FormField/Select/StateSelect";
import { statesToSelectOptions } from "@components/FormField/Select/StateSelect/util";
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
import { IRuleFormData } from "@interfaces/IStep3";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalRule.module.css";

interface ModalRuleProps extends ModalProps {
	onSubmit: (rule: IRuleFormData) => Promise<void>;
	analysisId: string;
	isLoading?: boolean;
	controlAction: IControlAction;
}
function ModalRule({
	analysisId,
	controlAction,
	open,
	onClose,
	onSubmit,
	isLoading = false
}: ModalRuleProps) {
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
		setRuleData(prev => ({
			...prev,
			states: stateList
		}));
	}, [selectedStates]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Rule Data

	const [ruleData, setRuleData] = useState<IRuleFormData>({
		name: "",
		code: "",
		states: [],
		types: [],
		hazard: hazardsOptions[0]
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Rule

	const handleSubmitRule = async () => {
		if (
			!ruleData.name ||
			!ruleData.code ||
			ruleData.states.length === 0 ||
			ruleData.types.length === 0 ||
			!ruleData.hazard
		) {
			toast.warning("A required field is empty.");
			return;
		}

		await onSubmit(ruleData);
		setRuleData({
			name: "",
			code: "",
			states: [],
			types: [],
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
			<ModalHeader onClose={onClose} title="New Rule" />
			<div className={styles.modal_rule_inputs}>
				<ModalInputs column="double">
					<Input
						label="Name"
						value={ruleData.name}
						onChange={(value: string) => setRuleData({ ...ruleData, name: value })}
						required
					/>
					<Input
						label="Code"
						value={ruleData.code}
						onChange={(value: string) => setRuleData({ ...ruleData, code: value })}
						required
					/>
				</ModalInputs>
				<ModalInputs column="double">
					{variables.map(variable => (
						<StateSelect
							key={variable.id}
							label={`${variable.name} State`}
							states={statesToSelectOptions(variable.states)}
							value={selectedStates[variable.id] || null}
							onChange={newValue => handleStateChange(variable.id, newValue)}
							optionsPosition="bottom"
						/>
					))}
				</ModalInputs>
				<ModalInputs>
					<TypesMultiSelect
						onChange={(types: SelectOption[]) =>
							setRuleData({ ...ruleData, types: types })
						}
						types={ruleData.types}
					/>
				</ModalInputs>
				<ModalInputs>
					<HazardSelect
						value={ruleData.hazard}
						onChange={(value: SelectOption | null) =>
							setRuleData({ ...ruleData, hazard: value })
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
					onClick={handleSubmitRule}
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

export default ModalRule;
