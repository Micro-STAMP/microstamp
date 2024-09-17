import Button from "@components/Button";
import { Input } from "@components/FormField";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { IVariableFormData, IVariableReadDto } from "@interfaces/IStep2";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalVariableProps extends ModalProps {
	onSubmit: (variable: IVariableFormData) => Promise<void>;
	title: string;
	isLoading?: boolean;
	variable?: IVariableReadDto;
	btnText?: string;
}
function ModalVariable({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	variable,
	btnText = "Confirm"
}: ModalVariableProps) {
	const [variableData, setVariableData] = useState<IVariableFormData>({
		name: variable?.name || "",
		code: variable?.code || ""
	});

	useEffect(() => {
		if (variable) {
			setVariableData({
				name: variable.name,
				code: variable.code
			});
		} else {
			setVariableData({
				name: "",
				code: ""
			});
		}
	}, [variable]);

	const handleSubmitVariable = async () => {
		if (!variableData.name || !variableData.code) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(variableData);
		setVariableData({
			name: variable?.name || "",
			code: variable?.code || ""
		});
		onClose();
	};

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={variableData.name}
					onChange={(value: string) => setVariableData({ ...variableData, name: value })}
					required
				/>
				<Input
					label="Code"
					value={variableData.code}
					onChange={(value: string) => setVariableData({ ...variableData, code: value })}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitVariable}
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

export default ModalVariable;
