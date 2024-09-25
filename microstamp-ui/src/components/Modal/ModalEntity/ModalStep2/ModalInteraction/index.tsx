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
import { IInteractionFormData, IInteractionReadDto } from "@interfaces/IStep2";
import {
	interactionTypesSelectOptions,
	interactionTypeToSelectOption
} from "@interfaces/IStep2/IInteraction/Enums";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalInteractionProps extends ModalProps {
	onSubmit: (interaction: IInteractionFormData) => Promise<void>;
	title: string;
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
	btnText = "Confirm"
}: ModalInteractionProps) {
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
				interactionType: interactionTypeToSelectOption(
					interaction.interactionType
				)
			});
		} else {
			setInteractionData({
				name: "",
				code: "",
				interactionType: interactionTypesSelectOptions[0]
			});
		}
	}, [interaction]);

	const handleSubmitInteraction = async () => {
		if (
			!interactionData.name ||
			!interactionData.code ||
			!interactionData.interactionType
		) {
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
					options={interactionTypesSelectOptions}
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
