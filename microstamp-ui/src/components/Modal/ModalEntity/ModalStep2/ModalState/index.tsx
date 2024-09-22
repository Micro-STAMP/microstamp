import Button from "@components/Button";
import { Input } from "@components/FormField";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { IStateFormData, IStateReadDto } from "@interfaces/IStep2";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalStateProps extends ModalProps {
	onSubmit: (state: IStateFormData) => Promise<void>;
	title: string;
	isLoading?: boolean;
	state?: IStateReadDto;
	btnText?: string;
}
function ModalState({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	state,
	btnText = "Confirm"
}: ModalStateProps) {
	const [stateData, setStateData] = useState<IStateFormData>({
		name: state?.name || "",
		code: state?.code || ""
	});

	useEffect(() => {
		if (state) {
			setStateData({
				name: state.name,
				code: state.code
			});
		} else {
			setStateData({
				name: "",
				code: ""
			});
		}
	}, [state]);

	const handleSubmitState = async () => {
		if (!stateData.name || !stateData.code) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(stateData);
		setStateData({
			name: state?.name || "",
			code: state?.code || ""
		});
		onClose();
	};

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={stateData.name}
					onChange={(value: string) => setStateData({ ...stateData, name: value })}
					required
				/>
				<Input
					label="Code"
					value={stateData.code}
					onChange={(value: string) => setStateData({ ...stateData, code: value })}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitState}
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

export default ModalState;
