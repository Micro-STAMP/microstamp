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
import { IConnectionActionFormData, IConnectionActionReadDto } from "@interfaces/IStep2";
import {
	connectionActionTypesSelectOptions,
	connectionActionTypeToSelectOption
} from "@interfaces/IStep2/IConnectionAction/Enums";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalConnectionActionProps extends ModalProps {
	onSubmit: (connectionAction: IConnectionActionFormData) => Promise<void>;
	title: string;
	isLoading?: boolean;
	connectionAction?: IConnectionActionReadDto;
	btnText?: string;
}
function ModalConnectionAction({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	connectionAction,
	btnText = "Confirm"
}: ModalConnectionActionProps) {
	const [connectionActionData, setConnectionActionData] = useState<IConnectionActionFormData>({
		name: connectionAction?.name || "",
		code: connectionAction?.code || "",
		connectionActionType: connectionAction
			? connectionActionTypeToSelectOption(connectionAction.connectionActionType)
			: connectionActionTypesSelectOptions[0]
	});

	useEffect(() => {
		if (connectionAction) {
			setConnectionActionData({
				name: connectionAction.name,
				code: connectionAction.code,
				connectionActionType: connectionActionTypeToSelectOption(
					connectionAction.connectionActionType
				)
			});
		} else {
			setConnectionActionData({
				name: "",
				code: "",
				connectionActionType: connectionActionTypesSelectOptions[0]
			});
		}
	}, [connectionAction]);

	const handleSubmitConnectionAction = async () => {
		if (
			!connectionActionData.name ||
			!connectionActionData.code ||
			!connectionActionData.connectionActionType
		) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(connectionActionData);
		setConnectionActionData({
			name: connectionAction?.name || "",
			code: connectionAction?.code || "",
			connectionActionType: connectionAction
				? connectionActionTypeToSelectOption(connectionAction.connectionActionType)
				: connectionActionTypesSelectOptions[0]
		});
		onClose();
	};

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={connectionActionData.name}
					onChange={(value: string) =>
						setConnectionActionData({ ...connectionActionData, name: value })
					}
					required
				/>
				<Input
					label="Code"
					value={connectionActionData.code}
					onChange={(value: string) =>
						setConnectionActionData({ ...connectionActionData, code: value })
					}
					required
				/>
				<Select
					label="Type"
					options={connectionActionTypesSelectOptions}
					onChange={(connectionAction: SelectOption | null) =>
						setConnectionActionData({
							...connectionActionData,
							connectionActionType: connectionAction
						})
					}
					value={connectionActionData.connectionActionType}
					optionsPosition="top"
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitConnectionAction}
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

export default ModalConnectionAction;
