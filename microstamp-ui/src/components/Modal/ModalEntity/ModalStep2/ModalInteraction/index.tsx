import Button from "@components/Button";
import { Input, Select } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { IComponentReadDto, IInteractionFormData, IInteractionReadDto } from "@interfaces/IStep2";
import {
	IInteractionType,
	interactionTypesSelectOptions,
	interactionTypeToSelectOption
} from "@interfaces/IStep2/IInteraction/Enums";
import { useEffect, useMemo, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalInteractionProps extends ModalProps {
	onSubmit: (interaction: IInteractionFormData) => Promise<void>;
	title: string;
	source: IComponentReadDto;
	target: IComponentReadDto;
	isLoading?: boolean;
	interaction?: IInteractionReadDto;
	btnText?: string;
}
function ModalInteraction({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	interaction,
	source,
	target,
	btnText = "Confirm"
}: ModalInteractionProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Get Valid Interaction Types

	const getValidInteractionTypes = (
		source: IComponentReadDto,
		target: IComponentReadDto
	): SelectOption[] => {
		const sourceIsEnv = source.type.toString() === "Environment";
		const targetIsEnv = target.type.toString() === "Environment";

		let types: IInteractionType[] = [];

		if (sourceIsEnv) {
			types = [IInteractionType.PROCESS_INPUT, IInteractionType.DISTURBANCE];
		} else if (targetIsEnv) {
			types = [IInteractionType.PROCESS_OUTPUT];
		} else {
			types = [
				IInteractionType.CONTROL_ACTION,
				IInteractionType.FEEDBACK,
				IInteractionType.COMMUNICATION_CHANNEL
			];
		}
		return types.map(type => interactionTypeToSelectOption(type));
	};

	const interactionTypeOptions = useMemo(() => {
		return getValidInteractionTypes(source, target);
	}, [source, target, interaction, open]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Interaction Data

	const [interactionData, setInteractionData] = useState<IInteractionFormData>({
		name: interaction?.name || "",
		code: interaction?.code || "",
		interactionType: interaction
			? interactionTypeToSelectOption(interaction.interactionType)
			: interactionTypesSelectOptions[0]
	});

	useEffect(() => {
		if (interaction) {
			setInteractionData({
				name: interaction.name,
				code: interaction.code,
				interactionType: interactionTypeToSelectOption(interaction.interactionType)
			});
		} else {
			setInteractionData({
				name: "",
				code: "",
				interactionType: interactionTypeOptions[0]
			});
		}
	}, [interaction]);

	useEffect(() => {
		if (
			interactionData.interactionType &&
			!interactionTypeOptions.find(
				option => option.value === interactionData.interactionType?.value
			)
		) {
			setInteractionData(data => ({
				...data,
				interactionType: interactionTypeOptions[0]
			}));
		}
	}, [interactionTypeOptions]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Submit Interaction

	const handleSubmitInteraction = async () => {
		if (!interactionData.name || !interactionData.code || !interactionData.interactionType) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(interactionData);
		setInteractionData({
			name: interaction?.name || "",
			code: interaction?.code || "",
			interactionType: interaction
				? interactionTypeToSelectOption(interaction.interactionType)
				: interactionTypesSelectOptions[0]
		});
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={interactionData.name}
					onChange={(value: string) =>
						setInteractionData({ ...interactionData, name: value })
					}
					required
				/>
				<Input
					label="Code"
					value={interactionData.code}
					onChange={(value: string) =>
						setInteractionData({ ...interactionData, code: value })
					}
					required
				/>
				<Select
					label="Type"
					options={interactionTypeOptions}
					onChange={(interaction: SelectOption | null) =>
						setInteractionData({
							...interactionData,
							interactionType: interaction
						})
					}
					value={interactionData.interactionType}
					optionsPosition="top"
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitInteraction}
					isLoading={isLoading}
					size="small"
					icon={CheckIcon}
				>
					{btnText}
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalInteraction;
