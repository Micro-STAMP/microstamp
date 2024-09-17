import Button from "@components/Button";
import { Input } from "@components/FormField";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { ILossFormData, ILossReadDto } from "@interfaces/IStep1";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalLossProps extends ModalProps {
	onSubmit: (loss: ILossFormData) => Promise<void>;
	title: string;
	isLoading?: boolean;
	loss?: ILossReadDto;
	btnText?: string;
}
function ModalLoss({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	loss,
	btnText = "Confirm"
}: ModalLossProps) {
	const [lossData, setLossData] = useState<ILossFormData>({
		name: loss?.name || "",
		code: loss?.code || ""
	});

	useEffect(() => {
		if (loss) {
			setLossData({
				name: loss.name,
				code: loss.code
			});
		} else {
			setLossData({
				name: "",
				code: ""
			});
		}
	}, [loss]);

	const handleSubmitLoss = async () => {
		if (!lossData.name || !lossData.code) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(lossData);
		setLossData({
			name: loss ? lossData.name : "",
			code: loss ? lossData.code : ""
		});
		onClose();
	};

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={lossData.name}
					onChange={(value: string) => setLossData({ ...lossData, name: value })}
					required
				/>
				<Input
					label="Code"
					value={lossData.code}
					onChange={(value: string) => setLossData({ ...lossData, code: value })}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitLoss}
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

export default ModalLoss;
